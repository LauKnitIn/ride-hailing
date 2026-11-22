# emparejamiento-service: tabla de separación (Etapa 1)

Servicio extraído del monolito modular `matching_rides`. El monolito se conserva intacto como referencia.

| Dato | Valor |
|---|---|
| Carpeta | `emparejamientoOfertas/` |
| Package raíz | `com.ride_hailing.emparejamientoOfertas` |
| Puerto | 8082 |
| Base de datos | PostgreSQL propia (`emparejamiento-db`, puerto 5434) |

## 1. El corte: qué va a este servicio

| Paquete / clase original | Destino | Notas |
|---|---|---|
| `application/` (4 servicios, 4 puertos de entrada, 3 de salida) | Se copia | Sin cambios |
| `domain/` (model, exception, factory, service) | Se copia | Sin cambios |
| `infrastructure/EmparejamientoOfertasConfig` | Se copia | Registra los beans del hexágono |
| `infrastructure/entrada/web` (DTOs, mapper) | Se copia | Sin cambios |
| `EmparejamientoOfertasController` | Se copia y se edita | Se quitan `ViajeUseCase` y `Viaje`; `rechazar` recibe el origen en el body |
| `infrastructure/salida/persistence` (entity, JPA repository, mapper, adapter) | Se copia | Sin cambios |
| `EmparejamientoPersistenceConfig` | Se elimina | Existía por tener varios DataSource en el monolito |
| `ConductoresAdapter` | Se reemplaza por `ConductoresPendiente` | Importaba `ConductorUseCase` de conductores |
| `AsignacionViajeAdapter` | Se reemplaza por `AsignacionViajePendiente` | Importaba `ViajeUseCase` de viajes |
| `ViajeSolicitadoListener` | Se elimina por ahora | Dependía de un evento en memoria de viajes |
| `shared.ManejadorErroresGlobal` | Se duplica como `ManejadorErroresEmparejamiento` | Solo con las excepciones de emparejamiento |

## 2. Tests

| Test | Destino |
|---|---|
| `EmparejamientoCasosDeUsoTest`, `AsignacionViajePortFake`, `OfertaRepositoryFalso` | Se copian sin cambios (no usan Spring ni BD) |
| `EmparejamientoApplicationTests` (`contextLoads`) | Se conserva, en el package `com.ride_hailing.emparejamientoOfertas`; requiere la BD arriba |
| `EmparejamientoFlujoCompletoTest` | **Se queda en el monolito**: usa `/api/viajes`, `/api/conductores`, `/api/pasajeros`, `/api/tarifas` y `/api/calificaciones` en un mismo `MockMvc` |

## 3. Brechas intencionales (se resuelven en la Etapa 2)

| Brecha | Consecuencia hoy | Reemplazo previsto |
|---|---|---|
| `ConductoresPendiente` devuelve lista vacía | Todo emparejamiento termina sin oferta (204) | Cliente hacia `GET /api/conductores/disponibles` de conductores-service |
| `AsignacionViajePendiente` solo escribe en el log | Aceptar una oferta no asigna el viaje | Notificación a viajes-service |
| `ViajeSolicitadoListener` eliminado | Nadie inicia el emparejamiento automáticamente; solo `POST /api/ofertas/emparejamientos` | Evento o llamada desde viajes-service |
| `rechazar` recibe el origen en el body (`RechazarOfertaRequest`, temporal) | El cliente debe enviar latitud y longitud | Puerto de salida hacia viajes, o guardar el origen en la `Oferta` |

## 4. Decisión de arquitectura: duplicación de código compartido

Igual que en conductores: duplicar el manejador de errores es consecuencia de *Database per Service* y del despliegue independiente. Un *shared kernel* queda como decisión pendiente del equipo.

## 5. Verificación

- `grep -rnE "import com\.ride_hailing\.(viajes|pasajeros|tarifas|calificacion|conductores|shared)" src` devuelve cesro resultados.
- `./mvnw test` pasa en verde (4 tests de casos de uso y `contextLoads`).
- El servicio arranca solo en el puerto 8082.
- Pruebas manuales: `POST /api/ofertas/emparejamientos` responde 204, `GET /api/ofertas/viaje/viaje-1` responde `[]` y `POST /api/ofertas/no-existe/aceptar` responde 404.
---

# Emparejamiento — Etapa 2 (Comunicación)

> **Actualización de datos**: en Etapa 2 el puerto HTTP pasó a **8086** y la BD a **5436** (Etapa 1 decía 8082 / 5434; quedan así por historia). Puerto gRPC: **no expone**.

## 6. Corte de Etapa 2

| Puerto de salida | Antes (Etapa 1 — "Pendiente") | Ahora (Etapa 2 — gRPC + Kafka + CB) |
|---|---|---|
| `ConductoresPort.obtenerConductoresDisponibles()` | `ConductoresPendiente` → lista vacía | `ConductoresGrpc` → `ConductorGrpc.ConductoresDisponibles` a `:9191` con CB + deadline 5s |
| `AsignacionViajePort.asignar(viajeId, conductorId)` | `AsignacionViajePendiente` → solo log | `AsignacionViajeGrpc` → `ViajeGrpc.AsignarConductor` a `:9192` con CB + deadline 5s + traducción de errores |
| `AsignacionViajePort.rechazarPorFaltaDeConductores(viajeId, motivo)` | `AsignacionViajePendiente` → solo log | `AsignacionViajeGrpc` → `ViajeGrpc.RechazarPorFaltaDeConductores` a `:9192` |
| `ViajeSolicitadoListener` (evento interno) | eliminado | `ViajesSolicitadosKafkaConsumer` → topic `viajes.solicitados` (JSON) |
| `rechazar` recibe origen en body (`RechazarOfertaRequest`) | TEMPORAL | **Opción A**: origen persistido en la Oferta; `POST /{id}/rechazar` sin body |

## 7. Decisiones de Etapa 2

### Opción A — El origen del viaje se guarda en la Oferta
- `Oferta` incorpora `origenLatitud` y `origenLongitud`, persistidos en `ofertas_emparejamiento`.
- `POST /api/ofertas/{id}/rechazar` ya no recibe body; el origen se toma de la oferta rechazada y se reutiliza para proponer al siguiente conductor.
- Razón: HU-04 (rechazo del conductor), el reintento pertenece al contexto de Emparejamiento, y cada servicio debe escalar/fallar de forma independiente.
- `POST /api/ofertas/emparejamientos` también persiste el origen.

### Circuit Breaker programático (Resilience4j)
- `conductoresServiceCircuitBreaker`: ignora `NOT_FOUND`.
- `viajesServiceCircuitBreaker`: ignora `NOT_FOUND` y `FAILED_PRECONDITION` (reglas de negocio, no fallos de infraestructura).
- Ambos: `slidingWindowSize=10`, `minCalls=5`, `failureRate=50%`, `waitOpen=10s`, `halfOpen=3`, deadline 5s.
- Traducción de errores gRPC en `AsignacionViajeGrpc`:
  - `NOT_FOUND` → `ViajeNoEncontradoException`
  - `FAILED_PRECONDITION` → `ReglaDeNegocioViajeException`
  - `INVALID_ARGUMENT` → `IllegalArgumentException`
  - default → `ServicioNoDisponibleException` → 503

### Idempotencia Kafka
- `ViajesSolicitadosKafkaConsumer` verifica `OfertaRepositoryPort.existePorViajeId(viajeId)` antes de procesar.

### Eureka
- Canal gRPC estático (`static://localhost:9191` y `static://localhost:9192`); Eureka no enruta gRPC.
- Registra `EMPAREJAMIENTO-SERVICE` en Eureka para el Gateway (`lb://emparejamiento-service`).

## 8. Brechas de Etapa 2

| Brecha | Consecuencia | Mitigación actual |
|---|---|---|
| Consistencia eventual entre `AsignarConductor` (gRPC) y `viajes.asignados` (Kafka) | Conductor sigue DISPONIBLE unos instantes | Aceptable para el dominio; Kafka at-least-once lo reconcilia |
| `viajes.cancelados` puede traer `conductorId=null` | El consumer de conductores podría fallar | Se ignora el mensaje cuando `conductorId` es null |
| Kafka at-least-once | Un `viajes.solicitados` podría reprocesarse | `existePorViajeId` en el consumer |

## 9. Verificación end-to-end (Nivel 7B)

- `Conductor registrado y DISPONIBLE → visible por gRPC en 9191` ✅
- `viajes.solicitados → oferta PROPUESTA con conductorId no nulo` ✅
- `origen_latitud=4, origen_longitud=-73 persistidos en BD` ✅ (Opción A)
- `POST /api/ofertas/{id}/rechazar sin body → RECHAZADA + intento gRPC a viajes → 503 cuando viajes caído` ✅ (CB + 503)

## 10. Tests de Etapa 2

- `EmparejamientoCasosDeUsoTest`: 4/4 (ajustados a Opción A, sin lat/lon en rechazar).
- `ConductoresGrpcTest`: 5/5 (stub OK, UNAVAILABLE, CB apertura/cierre, NOT_FOUND no cuenta como fallo).
- `AsignacionViajeGrpcTest`: 7/7 (OK, NOT_FOUND, FAILED_PRECONDITION, UNAVAILABLE, ok=false, rechazarPorFaltaDeConductores, CB apertura).
- `ViajesSolicitadosKafkaConsumerTest`: 3/3 (payload válido, idempotencia, payload inválido no explota).
- Total: **20/20** con `mvn test`.

## 11. Puertos (Etapa 2)

| Servicio | HTTP | gRPC | BD |
|---|---|---|---|
| conductores | 8085 | 9191 | 5433 |
| emparejamiento | 8086 | — | 5436 |
| viajes | 8082 | 9192 | 5434 |
| tarifas | 8081 | 9194 | 5435 |
| pasajeros | 8083 | 9193 | 5435 |
| calificaciones | 8084 | — | — |
| eureka | 8761 | — | — |
| gateway | 8000 | — | — |
| kafka | 9092 | — | — |

## 12. Cómo arrancar (Etapa 2)

```bash
docker compose up -d ride-kafka
(cd emparejamientoOfertas && docker compose up -d)
(cd conductores && docker compose up -d)
(cd eureka-server && mvn spring-boot:run)          # 8761
(cd viajes && mvn spring-boot:run)                 # 8082 / gRPC 9192
(cd conductores && mvn spring-boot:run)            # 8085 / gRPC 9191
(cd emparejamientoOfertas && mvn spring-boot:run)  # 8086
(cd gateway && mvn spring-boot:run)                # 8000
