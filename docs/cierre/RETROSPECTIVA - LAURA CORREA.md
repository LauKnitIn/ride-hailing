# RETROSPECTIVA - Laura Correa

Proyecto: ride-hailing (Ingeniería de Software II, UPTC). Rol: Dev 1. Subdominios a mi cargo: conductores y emparejamiento_ofertas.

## Autoevaluación (1 = no lo logro, 4 = lo hago con autonomía)

RA1 Explico principios, patrones y estilos arquitectónicos .......... 3
RA2 Analizo ventajas, limitaciones y contextos de los estilos ........ 3
RA3 Diseño arquitecturas con modelos, diagramas y estándares ........ 3
RA5 Implemento arquitecturas modernas (microservicios, EDA, CQRS,
    DDD, hexagonal) ................................................ 3

**Evidencia por resultado de aprendizaje**

- **RA1 (3):** Puedo explicar por qué el dominio y la aplicación no deben importar nada de otros servicios (puertos y adaptadores), y cuándo usar gRPC síncrono frente a eventos en Kafka. Evidencia: `NOTAS.md` de conductores y de emparejamientoOfertas, con la tabla del corte, las brechas intencionales y las decisiones. Me falta soltura con patrones que no implementé (CQRS, Saga).
- **RA2 (3):** Analicé ventajas y límites de cada decisión. Ejemplos: la consistencia eventual entre `AsignarConductor` y el evento `viajes.asignados`, el ciclo de llamadas viajes↔conductores acotado con deadline y Circuit Breaker, y la entrega "al menos una vez" de Kafka, que obliga a consumidores idempotentes. Evidencia: `NOTAS.md` (riesgos y limitaciones) y la decisión de la opción A.
- **RA3 (3):** Diseñé los cortes y contratos de mis servicios (puertos, DTOs, contratos `.proto` compatibles con viajes, el endpoint `GET /api/ofertas/conductor/{id}/pendientes`). Evidencia: bounded contexts y diagrama de eventos del documento del equipo, y las tablas de corte. Me faltó producir diagramas propios (componentes, secuencia) en un estándar como C4 o UML; el diseño lo documenté sobre todo en texto y tablas.
- **RA5 (3):** Implementé microservicios con arquitectura hexagonal y DDD, con base de datos por servicio, gRPC con Circuit Breaker, consumidores Kafka idempotentes y registro en Eureka detrás del Gateway. Evidencia: carpetas `conductores/` y `emparejamientoOfertas/`, 4 y 21 tests en verde, la prueba de punta a punta por el Gateway con Docker del flujo de mis módulos (oferta automática al conductor, aceptación y cierre del viaje), y la integración con la interfaz del conductor. No probé a fondo los módulos de otros compañeros (tarifas, calificaciones). No implementé CQRS ni Outbox propio, por eso un 3 y no un 4. Parte de las tareas las hice con apoyo de asistentes de IA y de las guías de clase, así que no siempre partí de cero.

## Tres aprendizajes que me llevo

1. **Una frontera de servicio se corta por el puerto, no por el código.** En la etapa 1, `ViajeActivoPort` y `ConductoresPort` no cambiaron: solo reemplacé el adaptador por uno "pendiente" y después por uno gRPC. Gracias al hexágono, la lógica y los tests con fakes quedaron intactos.
2. **Separar no es lo mismo que comunicar, y cada comunicación cuesta.** Al pasar de llamadas en memoria a red aparecieron cosas que antes no existían: timeouts, circuitos abiertos, mensajes duplicados, conductores que quedan `DISPONIBLE` unos instantes tras ser asignados, y la necesidad de duplicar código como el manejador de errores. Entendí por qué "Database per Service" tiene un precio.
3. **Probar integrando enseña lo que los tests unitarios no.** Los tests pasaban, pero la prueba real con la interfaz mostró un hueco: el conductor no tenía cómo ver sus ofertas (tuvimos que añadir `/pendientes`). También aprendí lo práctico: puertos que chocan, `localhost` dentro de contenedores, y que 16 contenedores no caben en 5,6 GB de RAM.

## Una decisión de arquitectura que tomaría distinto hoy, y por qué

**Separar emparejamiento_ofertas sin identificar antes qué datos de otros dominios necesitaba, y tratar ofertas y emparejamiento como un solo servicio sin cuestionarlo.**

En la separación, el controller de ofertas consultaba el viaje solo para obtener el origen al rechazar una oferta. Lo resolví primero con una solución temporal (las coordenadas en el body) y luego con la opción A: guardar el origen en la `Oferta` al consumir `viajes.solicitados`. Elegí A porque quien rechaza es el conductor (HU-04), porque el reintento pertenece al contexto de emparejamiento y porque no obliga a Dev 2 a cambiar su contrato.

Hoy lo diseñaría así desde el principio: antes de cortar, listaría qué datos de otros dominios necesita cada caso de uso y los guardaría como copia local alimentada por eventos, en vez de descubrirlo al romperse la dependencia.

Sobre la frontera entre ofertas y emparejamiento: el equipo los definió como un solo bounded context, y lo justifico por cohesión (la oferta nace y muere dentro del proceso de emparejamiento y comparten reglas). Con más tiempo evaluaría si las ofertas, que el conductor consulta con polling, deberían escalar aparte del algoritmo de selección.

También revisaría que el consumidor de conductores use `auto-offset-reset: latest`: es simple, pero puede perder eventos si el servicio arranca tarde. Lo valoraría contra `earliest`, con la idempotencia ya garantizada.

## Lo que me falta por aprender y cómo lo haría

- **Saga, Outbox propio y consistencia distribuida.** Usé el Outbox de Dev 2 como consumidora, pero no lo implementé. Plan: rehacer los pasos 6 y 7 del taller de la Lección 6 sobre mi propio servicio, por ejemplo con un consumidor de `viajes.cancelados` que marque las ofertas como obsoletas, con compensación.
- **CQRS y modelos de lectura.** El endpoint `/pendientes` es una consulta pensada para el conductor; me falta ver cuándo conviene separar un modelo de lectura. Plan: estudiar un ejemplo y aplicarlo a una proyección de ofertas.
- **Diagramas estándar (C4, UML, secuencia).** Plan: documentar mis servicios con un diagrama de componentes y uno de secuencia del flujo de emparejamiento, y revisarlos con el asesor.
- **Seguridad y autorización.** Hoy cualquiera con un `conductorId` ve sus ofertas. Plan: aprender autenticación en el Gateway (JWT) y aplicarla.
- **Operación y despliegue.** Healthchecks, límites de memoria de las JVM y Dockerfile con usuario no root. Plan: aplicarlos al compose del equipo.
- **Pruebas de integración y de contrato automatizadas.** Mis pruebas de punta a punta fueron manuales y limitadas a mis módulos. Plan: escribir tests de contrato para los `.proto` y pruebas de integración con Testcontainers (Kafka y PostgreSQL), y coordinarme con el equipo para cubrir el flujo completo.
- **Dependo todavía de las guías y de asistencia para detalles de Spring Cloud y gRPC.** Plan: construir un servicio pequeño desde cero, sin copiar, para comprobar que lo domino.

## Mi aporte al proyecto de grupo (con evidencia de commits)

- **Etapa 1, separación (conductores y emparejamientoOfertas):** proyectos Maven independientes, base de datos propia por servicio, adaptadores "pendiente", manejadores de errores propios y `NOTAS.md` con la tabla del corte. Commit: `feat: separar conductores y emparejamientoOfertas (etapa 1)`.
- **Etapa 2, comunicación:** servidor gRPC de conductores, clientes gRPC con Circuit Breaker hacia viajes y conductores, consumidores Kafka idempotentes, cliente Eureka y opción A (origen en la oferta). Rama: `feat/comunicacion-conductores-emparejamiento`. Commit: `feat: ofertas pendientes por conductor, origen en la oferta y comunicacion gRPC/Kafka`.
- **Endpoint para el flujo del conductor:** `GET /api/ofertas/conductor/{id}/pendientes`, que surgió al probar la integración con la interfaz.
- **Pruebas:** 21 tests en emparejamiento (casos de uso con fakes, adaptadores con Mockito, consumidor Kafka) y 4 en conductores. Probé de punta a punta, con Docker Compose y por el Gateway, el flujo de mis dos módulos; las pruebas de tarifas, calificaciones y del resto del sistema no las ejecuté yo. También hice solicitudes de Bruno y un script de flujo para mis endpoints.
- **Integración con la interfaz:** conecté el flujo del conductor con el frontend (registro, disponibilidad, ofertas pendientes con polling, aceptar o rechazar). Commit/rama: [hash].
- **Integración y apoyo al equipo:** detección de choques de puertos (5435 compartido entre pasajeros y tarifas) y revisión de la dockerización.
