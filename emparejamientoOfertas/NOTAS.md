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