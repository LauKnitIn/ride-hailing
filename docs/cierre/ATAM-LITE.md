# Evaluación ligera de arquitectura (ATAM-lite)

## Contexto

Ride Hailing mantiene una arquitectura híbrida: una aplicación modular y
servicios independientes para capacidades como viajes, emparejamiento, tarifas
y calificaciones. Gateway y Eureka ofrecen entrada y descubrimiento; los
servicios consultan por REST/gRPC y propagan algunos hechos de viaje mediante
Kafka y Outbox. El taller prioriza modificabilidad, integridad, testabilidad,
interoperabilidad y disponibilidad práctica.

## Árbol de utilidad y escenarios

La importancia para el negocio y la dificultad se califican como Alta (A),
Media (M) o Baja (B).

| Atributo | Estímulo y respuesta esperada | Medida | Importancia / dificultad |
|---|---|---|---|
| Modificabilidad | Cambia la regla de tarifa; el equipo modifica y despliega Tarifas sin cambiar el código de los demás servicios. | Cambio localizado en un servicio; sus pruebas pasan y no cambia el contrato existente. | A / A |
| Integridad | Kafka queda indisponible durante una finalización de viaje; Viajes confirma el cambio y conserva el evento pendiente. | Cero eventos confirmados perdidos; evento publicado después de recuperar Kafka. | A / A |
| Disponibilidad práctica | Viajes no responde cuando se intenta calificar; Calificaciones falla de forma explícita y no guarda una calificación sin validación. | Respuesta de indisponibilidad en hasta 5 segundos; cero calificaciones aceptadas sin validación. | A / A |
| Interoperabilidad | Un servicio consulta un viaje inexistente o Viajes no está disponible. | Errores de negocio e indisponibilidad diferenciados; consulta gRPC con deadline de 5 segundos. | A / M |
| Testabilidad | Se modifica una regla de dominio de una capacidad. | Pruebas unitarias e integración del módulo pasan sin depender de bases o servicios remotos reales. | M / M |

## Análisis de escenarios A/A y A/M

### Modificabilidad — A/A

- **Decisión que lo sostiene (no-riesgo):** límites por servicio, contratos
  REST/gRPC y persistencia propia reducen el alcance de los cambios.
- **Qué podría fallar (riesgo):** la coexistencia con `ride_hailing/` puede
  duplicar reglas y permitir que los dos caminos de ejecución diverjan.
- **Sensibilidad:** contratos compartidos, dependencias entre servicios y
  disciplina para no importar clases internas de otro módulo.
- **Compromiso:** autonomía de despliegue mejora, pero aumenta la cantidad de
  configuración y pruebas de integración que mantener.

### Integridad — A/A

- **Decisión que lo sostiene (no-riesgo):** Transactional Outbox en Viajes
  guarda el cambio de viaje y el evento en la misma base, y publica después de
  confirmar Kafka.
- **Qué podría fallar (riesgo):** el broker único, duplicados, errores de
  serialización o eventos pendientes sin supervisión pueden retrasar la
  propagación.
- **Sensibilidad:** atomicidad de la escritura con Outbox, idempotencia de
  consumidores y política de reintentos.
- **Compromiso:** Outbox reduce el riesgo de pérdida, a cambio de una tabla,
  sondeo, reintentos y consistencia eventual.

### Disponibilidad práctica — A/A

- **Decisión que lo sostiene (no-riesgo):** Calificaciones verifica el viaje
  sincrónicamente por gRPC, aplica un Circuit Breaker y no convierte una caída
  de Viajes en una validación exitosa.
- **Qué podría fallar (riesgo):** Viajes es una dependencia obligatoria para
  registrar una calificación; si no responde, la escritura no puede continuar.
- **Sensibilidad:** deadline, umbrales y periodo de apertura del Circuit
  Breaker.
- **Compromiso:** se protege la integridad del negocio, aunque la disponibilidad
  de la escritura de calificación depende de otro servicio.

### Interoperabilidad — A/M

- **Decisión que lo sostiene (no-riesgo):** Gateway enruta rutas HTTP con Eureka
  y los servicios usan contratos gRPC tipados para consultas síncronas.
- **Qué podría fallar (riesgo):** nombres registrados, puertos y esquemas pueden
  quedar desalineados entre Gateway, cliente y proveedor.
- **Sensibilidad:** compatibilidad hacia atrás de contratos y configuración por
  entorno.
- **Compromiso:** contratos explícitos reducen ambigüedad, pero requieren
  coordinación antes de cambiar campos o comportamientos.

## Tres riesgos principales y mitigaciones

1. **Eventos retrasados sin visibilidad.** Kafka usa un broker local único y
   falta una alerta sobre el tamaño/antigüedad del Outbox. Mitigación: medir
   eventos pendientes y su antigüedad, registrar reintentos y probar recuperación
   del broker.
2. **Dependencia síncrona de Calificaciones hacia Viajes.** Una caída de Viajes
   impide registrar nuevas evaluaciones. Mitigación: mantener deadline y Circuit
   Breaker, observar sus transiciones y evaluar un evento de viaje completado
   solo si el equipo acepta consistencia eventual.
3. **Reglas duplicadas entre monolito y servicios.** La arquitectura híbrida
   puede producir comportamientos distintos. Mitigación: declarar qué ruta está
   activa, asignar un dueño por capacidad y ejecutar pruebas de contrato para
   cambios transversales.

## Límites de la evaluación

Las medidas indicadas son objetivos verificables, no resultados de pruebas de
carga ni de producción. El repositorio no muestra métricas de rendimiento o
trazabilidad distribuida configuradas; antes de atribuir cumplimiento se deben
instrumentar y medir esos escenarios.
