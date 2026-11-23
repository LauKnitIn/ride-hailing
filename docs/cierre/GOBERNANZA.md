# Gobernanza de arquitectura

## Guardarraíles de arquitectura

| Regla | Verificación automática | Cuándo | Estado |
|---|---|---|---|
| El dominio y la aplicación no importan clases de Spring, JPA, gRPC, Kafka ni adaptadores de infraestructura. | Prueba ArchUnit que prohíba dependencias hacia los paquetes de infraestructura y frameworks. | En cada pull request. | Propuesto; el repositorio sigue el patrón de puertos y adaptadores en Calificaciones, pero no se encontró una regla ArchUnit. |
| Cada servicio solo configura su propia base; las consultas a datos de otros servicios usan contratos remotos. | Prueba o script CI que revise las URLs JDBC por servicio y una regla de dependencias que prohíba importar repositorios internos de otros módulos. | En cada pull request. | Parcial: la propiedad de datos está documentada, falta automatizar la regla para todos los módulos. |
| Las migraciones de Calificaciones y Tarifas son la fuente del esquema; Hibernate valida y no actualiza la base. | Arranque de integración contra una base vacía, aplicación de Flyway y validación de Hibernate. | En cada cambio de entidad o migración. | Implementado en Calificaciones y Tarifas; otros servicios todavía tienen configuración distinta. |
| Los contratos HTTP/gRPC y los eventos no se cambian sin mantener compatibilidad o actualizar productor, consumidores y pruebas. | Pruebas de contrato y suites de los módulos afectados; revisar nombres de tópicos y códigos de error. | En cada cambio transversal. | Parcial: hay pruebas del flujo y contratos protobuf; falta una verificación de compatibilidad automatizada común. |

## Cómo se toman las decisiones

- **Cambio local a un módulo:** lo propone quien mantiene la capacidad, registra
  el motivo en el pull request y añade las pruebas correspondientes.
- **Cambio que cruza límites** (API, evento, esquema, protocolo o propietario de
  datos): se registra como ADR breve con contexto, alternativas, consecuencias y
  responsable; requiere revisión de al menos dos integrantes afectados.
- **Excepción a un guardarraíl:** se documenta como deuda técnica, con
  responsable, riesgo y fecha de revisión. No se convierte silenciosamente en
  precedente.
- **Revisión:** al cierre de cada fase o cuando cambien los atributos de calidad,
  el equipo reevalúa riesgos, matriz y ADR afectados.

## Deuda técnica reconocida

| Deuda | Por qué se aceptó | Riesgo | Cuándo revisarla |
|---|---|---|---|
| Coexisten el monolito modular y servicios independientes. | Permite evolucionar gradualmente sin reescritura completa. | Reglas duplicadas o diferencias de comportamiento entre rutas. | Antes de añadir una capacidad a ambos lados o de preparar una entrega integrada. |
| Kafka tiene un único broker local y Outbox no tiene métricas/alertas dedicadas. | Suficiente para el entorno académico y las pruebas actuales. | Indisponibilidad del broker y acumulación de eventos no detectadas a tiempo. | Antes de una demostración que dependa de eventos o de un entorno persistente. |
| La observabilidad distribuida es limitada y no existe una Saga del flujo actual. | No hay un proceso acordado que requiera compensar transacciones entre servicios. | Diagnóstico lento de fallos que cruzan varias llamadas; una Saga futura podría quedar sin trazabilidad. | Antes de añadir una operación distribuida con compensaciones o al integrar nuevos consumidores. |
| La configuración local usa valores por defecto sencillos para facilitar el laboratorio. | Reduce pasos de instalación local. | Riesgo si esos valores se reutilizan fuera del entorno de desarrollo. | Antes de cualquier despliegue compartido o productivo. |

## Radar tecnológico

- **Adoptar:** PostgreSQL y Flyway para persistencia relacional con esquema
  versionado; REST/gRPC para consultas cuyo resultado se necesita de inmediato.
- **Evaluar:** OpenTelemetry con métricas, trazas y logs correlacionados para
  diagnosticar llamadas entre Gateway, servicios y Kafka.
- **Evitar por ahora:** MongoDB para las calificaciones relacionales y Saga si
  no hay una operación multi-servicio que requiera compensación; ambos sumarían
  complejidad sin una necesidad actual demostrada.
