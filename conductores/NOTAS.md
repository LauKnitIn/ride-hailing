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