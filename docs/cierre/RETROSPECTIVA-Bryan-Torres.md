# Retrospectiva personal - Bryan Torres

**Proyecto:** Ride Hailing  
**Equipo:** LauKnitIn

## Autoevaluación

Escala: 1 = no lo logro, 4 = lo hago con autonomía.

| Resultado de aprendizaje | Nivel | Evidencia concreta |
|---|---:|---|
| RA1 Explico principios, patrones y estilos arquitectónicos | 4 | Implementé la arquitectura hexagonal en las partes de tarifas y calificaciones. Organicé dominio, aplicación y adaptadores, separé puertos primarios y secundarios y documenté cada paso en los commits y notas del repositorio. |
| RA2 Analizo ventajas, limitaciones y contextos de los estilos | 3 | Analicé cuándo conviene comunicar servicios de forma síncrona por gRPC y cuándo propagar cambios mediante Kafka. También incorporé circuit breakers y reconocí sus límites y costos operativos. |
| RA3 Diseño arquitecturas con modelos, diagramas y estándares | 3 | Participé en el diseño de los servicios, su persistencia, sus contratos gRPC y su integración con Eureka y el API Gateway. Implementé configuraciones alineadas con los puertos HTTP y gRPC definidos en Docker Compose. |
| RA5 Implemento arquitecturas modernas: microservicios, EDA, CQRS, DDD y hexagonal | 3 | Implementé dominio hexagonal, persistencia JPA, Kafka, gRPC, Eureka, PostgreSQL y circuit breakers. Realicé pruebas con repositorios fake para mantener el núcleo independiente de infraestructura. |

## Tres aprendizajes que me llevo

1. **Arquitectura hexagonal:** aprendí a proteger el dominio con puertos y adaptadores, separando la lógica de negocio de las tecnologías de entrada y salida.
2. **Microservicios:** comprendí que distribuir el sistema aporta independencia de despliegue, pero también introduce latencia, fallos parciales, consistencia eventual y mayor complejidad operativa.
3. **DDD y comunicación dirigida por eventos:** entendí la importancia de un lenguaje ubicuo, de limitar cada modelo de datos al contexto correspondiente y de usar eventos con nombres claros para comunicar hechos como la finalización o cancelación de un viaje.

## Una decisión de arquitectura que tomaría distinto hoy, y por qué

Hoy escogería una arquitectura modular con enfoque hexagonal dentro de un monolito modular al comenzar el proyecto. Trabajaríamos con fronteras claras y puertos desde el inicio, lo que permitiría trabajar en paralelo sin pagar desde el primer momento el costo operativo de numerosos microservicios.

Después extraería los módulos a microservicios solamente cuando existiera una necesidad concreta: escalado independiente, despliegue separado o autonomía de equipo. Aunque la comunicación distribuida es valiosa, para este proyecto quizá conviene no dividir cada contexto sin medir primero su beneficio real.

## Lo que me falta por aprender y cómo lo haría

Me falta profundizar en **Sagas y consistencia distribuida**. Las estudiaría implementando una prueba completa entre el emparejamiento, la asignación de un conductor y la cancelación o finalización de un viaje.

Mi plan sería:

1. Definir el proceso como saga coreografiada o como orquestada.
2. Modelar los estados compensatorios y los eventos que pueden causar compensación.
3. Crear pruebas para fallos parciales y reintentos.
4. Garantizar idempotencia en consumidores y manejar mensajes duplicados.
5. Medir el tiempo de recuperación y la cantidad de estados inconsistentes.

## Mi aporte al proyecto de grupo

Mi trabajo se centró principalmente en **calificaciones, tarifas, viajes, comunicación distribuida y persistencia**.

### Evidencias por commit

- `423a9dd` - implementé el cálculo de tarifas.
- `423a9dd` - agregué el dominio necesario para calcular la tarifa de un viaje.
- `0c65da8` - implementé la validación de puntajes para las calificaciones.
- `b922691` - incorporé pruebas unitarias para puntajes y cálculos de tarifa.
- `19fffcd` - apliqué lenguaje ubicuo en el dominio.
- `4ac0804` - creé el puerto primario `CalculoTarifaUseCase`.
- `e20f2a1` - implementé el puerto secundario y el adaptador de viajes para tarifas.
- `d95bf5e` - reorganicé tarifas con capas hexagonales.
- `feb00aa` - probé el núcleo de tarifas usando repositorio fake.
- `7c77b83` - implementé el puerto primario `CalificarConductorUseCase`.
- `65839ba` - implementé el adaptador secundario de viajes para calificaciones.
- `716d4bf` - implementé `CalificarConductorApplicationService`.
- `406e82e` - añadí dependencias JPA, PostgreSQL y configuración de persistencia.
- `406e82e` - incorporé servicios y adaptadores a la arquitectura.
- `406e82e` - configuré H2 y las propiedades necesarias.
- `3fd3e95` - añadí validación y H2 a la configuración de pruebas.
- `7bac993` - implementé controladores de calificación y tarifa con sus modelos HTTP.
- `5f82416` - implementé servicios de calificaciones y tarifas con persistencia JPA.
- `74e5aa4` - configuré PostgreSQL y la tabla `tarifas_viaje`.
- `0ae7cbd` - implementé el consumidor Kafka de eventos de viaje y manejo de finalización y cancelación.
- `d70ba98` - implementé el circuit breaker para el servicio de viajes.
- `a7d2599` - implementé comunicación gRPC en el servicio de viajes y el manejo de excepciones.
- `98d1e64` - incorporé Eureka y su configuración al servicio.
- `c95312c` - implementé la consulta de calificaciones por conductor.
- `b4a3b6c` - corregí el URI del servicio de calificaciones.
- `5cc1c61` - corregí las solicitudes de calificaciones en el archivo Bruno.

## Qué aprendí trabajando en equipo

Aprendí que dividir el trabajo por módulos permite avanzar más rápido, pero requiere acuerdos explícitos sobre contratos, nombres, puertos, bases de datos y eventos. También entendí que una decisión técnica debe quedar documentada y comprobada, porque una integración puede funcionar en el entorno de una persona y fallar al integrarse con el de las demás.

## Siguientes pasos personales

- Profundizar en seguridad y autenticación entre servicios.
- Profundizar en Sagas y consistencia distribuida.
- Medir latencia y disponibilidad con pruebas de carga.
- Mejorar la trazabilidad desde una solicitud HTTP hasta los servicios gRPC y eventos Kafka.
- Escribir ADR más claros con contexto, decisión, alternativa descartada y consecuencias negativas.
