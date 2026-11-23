# conductores-service: tabla de separación (Etapa 1)

Servicio extraído del monolito modular `matching_rides`. El monolito se conserva intacto como referencia.

| Dato | Valor |
|---|---|
| Carpeta | `conductores/` |
| Package raíz | `com.ride_hailing.conductores` |
| Puerto | 8081 |
| Base de datos | PostgreSQL propia (`conductores-db`, puerto 5433) |

## 1. El corte: qué va a este servicio

| Paquete / clase original | Destino | Notas |
|---|---|---|
| `application/` (`ConductorService`, `ConductorFactory`, puertos de entrada y salida) | Se copia | Sin cambios |
| `domain/model`, `domain/excepcion`, `domain/service` | Se copia | Sin cambios |
| `infrastructure/ConductoresConfig` | Se copia | Registra los beans del hexágono |
| `infrastructure/entrada/web` (controller, DTOs, mapper) | Se copia | Se agregan dos endpoints temporales |
| `infrastructure/salida/persistence` (entity, JPA repository, mapper, adapter) | Se copia | Sin cambios |
| `ConductoresPersistenceConfig` | Se elimina | Existía por tener varios DataSource en el monolito; aquí basta el autoconfigurado de Spring Boot |
| `ViajeActivoAdapter` | Se reemplaza por `ViajeActivoPendiente` | Importaba `ViajeUseCase` de viajes |
| `ViajeConductorListener` | Se elimina por ahora | Dependía de eventos en memoria de viajes; vuelve en la Etapa 2 |
| `domain/repository/*` (`ConductorRepository`, `ViajeActivoCheckerRepository`) | Se descarta | Código muerto: el `grep` confirmó que nadie las usaba |
| `shared.ManejadorErroresGlobal` | Se duplica como `ManejadorErroresConductores` | Solo con las excepciones de conductores |

## 2. Tests

| Test | Destino |
|---|---|
| `ConductorRepositoryFalso`, `ConductorServiceConFalsoTest` | Se copian sin cambios (no usan Spring ni BD) |
| `ConductoresApplicationTests` (`contextLoads`) | Se conserva, movido al package `com.ride_hailing.conductores`; requiere la BD arriba |

## 3. Brechas intencionales (se resuelven en la Etapa 2)

| Brecha | Consecuencia hoy | Reemplazo previsto |
|---|---|---|
| `ViajeActivoPendiente` siempre devuelve `false` | La regla "no desactivar con viaje activo" no se hace cumplir desde este servicio | Cliente hacia viajes-service |
| Listener de `ViajeAsignado`, `ViajeFinalizado` y `ViajeCancelado` eliminado | Nadie marca ni libera al conductor automáticamente | Consumo de eventos o llamadas desde viajes |
| Endpoints temporales `PUT /{id}/en-viaje` y `PUT /{id}/liberar` | Permiten probar `marcarEnViaje` y `liberarDeViaje` a mano | Se retiran o quedan como API interna |

## 4. Decisión de arquitectura: duplicación de código compartido

Duplicar el manejador de errores no es un descuido: es la primera consecuencia real de *Database per Service* y del despliegue independiente. Un `@ControllerAdvice` compartido implicaría una librería común versionada por separado (un *shared kernel*). Eso queda fuera del alcance de esta etapa, pero se anota como decisión pendiente del equipo.

## 5. Verificación

- `grep -rnE "ride_hailing\.(viajes|pasajeros|tarifas|calificacion|emparejamientoOfertas|shared)" src` devuelve cero resultados.
- `./mvnw test` pasa en verde (3 tests de lógica y `contextLoads`).
- El servicio arranca solo en el puerto 8081, sin `viajes` en el classpath.
---

# Conductores — Etapa 2 (Comunicación)

> **Actualización de datos**: en Etapa 2 el puerto HTTP pasó a **8085** y el gRPC a **9191**; la BD sigue en **5433**. Puerto HTTP de Etapa 1 ya era 8085, sin cambio. La BD ya era 5433, sin cambio.

## 6. Corte de Etapa 2

| Puerto de salida | Antes (Etapa 1 — "Pendiente") | Ahora (Etapa 2 — gRPC + Kafka + CB) |
|---|---|---|
| `ViajeActivoPort.tieneViajeActivo(DriverId)` | `ViajeActivoPendiente` → siempre `false` | `ViajeActivoGrpc` → `ViajeGrpc.ConductorTieneViajeEnCurso` a `:9192` con CB + deadline 5s |
| `ViajeConductorListener` (evento interno) | eliminado | `ViajesKafkaConsumer` → topics `viajes.asignados`, `viajes.finalizados`, `viajes.cancelados` (JSON) |
| `PUT /{id}/en-viaje` y `PUT /{id}/liberar` | presentes | retirados; el cambio de estado lo disparan eventos Kafka |

## 7. Servicio gRPC expuesto (9191)

- `ConductorGrpc.UbicacionActual(ConductorRequest{conductorId}) → UbicacionResponse{tieneUbicacion, latitud, longitud}`. Si no existe → `NOT_FOUND`.
- `ConductorGrpc.ConductoresDisponibles(DisponiblesRequest{}) → DisponiblesResponse{repeated ConductorDisponible{conductorId, latitud, longitud}}`. Filtra sin ubicación.
- `@Service` sobre `ConductorGrpcGrpc.ConductorGrpcImplBase` (Spring gRPC autodetecta beans `BindableService`).

## 8. Decisiones de Etapa 2

### Kafka como disparador de cambios de estado
- `viajes.asignados` → `marcarEnViaje(conductorId)`. Captura `ConductorNoEncontradoException` e `IllegalStateException` (idempotencia).
- `viajes.finalizados` → `liberarDeViaje(conductorId)`.
- `viajes.cancelados` → `liberarDeViaje(conductorId)` si `conductorId != null`; si `null`, se ignora.
- Un mensaje malformado nunca tumba el consumer: siempre capturar y loguear.

### Circuit Breaker programático (Resilience4j)
- `viajesServiceCircuitBreaker`: ignora `NOT_FOUND` (conductor sin viaje activo es caso normal).
- Ventana 10, minCalls 5, failureRate 50%, waitOpen 10s, halfOpen 3, deadline 5s.

### Eureka
- Registra `CONDUCTORES-SERVICE` en 8761 para el Gateway (`lb://conductores-service`).
- Canal gRPC estático (`static://localhost:9192`), porque Eureka no enruta gRPC.

## 9. Brechas de Etapa 2

| Brecha | Consecuencia | Mitigación actual |
|---|---|---|
| Consistencia eventual entre `AsignarConductor` y `viajes.asignados` | Conductor sigue DISPONIBLE unos instantes | Aceptable; Kafka at-least-once reconcilia |
| `viajes.cancelados` con `conductorId=null` | Podría fallar si no se valida | Se ignora el mensaje |
| Kafka at-least-once | `marcarEnViaje` podría repetirse | `IllegalStateException` capturada (idempotente) |

## 10. Verificación end-to-end (Nivel 6)

- `grpcurl list localhost:9191` → `ConductorGrpc`, `grpc.health.v1.Health`, `grpc.reflection.v1.ServerReflection` ✅
- `grpcurl ConductorGrpc/ConductoresDisponibles` devuelve el conductor registrado ✅
- `grpcurl ConductorGrpc/UbicacionActual` con id inexistente → `NOT_FOUND` ✅
- Publicar en `viajes.asignados` → conductor pasa a `EN_VIAJE` ✅
- Publicar en `viajes.finalizados` → conductor vuelve a `DISPONIBLE` ✅
- Consumer group `conductores-service` con 3 topics y `LAG=0` ✅

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
(cd conductores && docker compose up -d)
(cd eureka-server && mvn spring-boot:run)          # 8761
(cd viajes && mvn spring-boot:run)                 # 8082 / gRPC 9192
(cd conductores && mvn spring-boot:run)            # 8085 / gRPC 9191
