## Autoevaluación (1 = no lo logro, 4 = lo hago con autonomía)
RA1 Explico principios, patrones y estilos arquitectónicos .......... 3
RA2 Analizo ventajas, limitaciones y contextos de los estilos ........ 4
RA3 Diseño arquitecturas con modelos, diagramas y estándares ........ 3
RA5 Implemento arquitecturas modernas (microservicios, EDA, CQRS,
    DDD, hexagonal) ................................................ 4
Para cada uno: una evidencia concreta (taller, commit, documento).

## Tres aprendizajes que me llevo
1. En un sistema distribuido la falla es lo normal, y cada patrón responde a una falla
   concreta: Circuit Breaker contra un servicio caído, Outbox contra Kafka caído, y
   DLT más consumidor idempotente contra mensajes malos o repetidos.
2. Publicar eventos en tópicos desacopla de verdad.
3. El límite del agregado decide dónde se corta el microservicio.

## Una decisión de arquitectura que tomaría distinto hoy, y por qué
Definiría los contratos REST de cada servicio a partir de las pantallas que los van a
usar, y no solo desde el dominio.

## Lo que me falta por aprender y cómo lo haría
- Pruebas de integración y de contrato.
- Seguridad. El backend no tiene autenticación y los frontends solo recuerdan al
  usuario en el navegador.

## Mi aporte al proyecto de grupo (con evidencia de commits)
Fui Dev 2, responsable de los subdominios viajes y pasajeros. Tengo 50 commits en
LauKnitIn/ride-hailing:
- DDD (25–28 sep): value object EstadoViaje (3d965ce), servicio de dominio
  TransicionEstadoViajeService (91a1ba3), agregado Viaje (ca90269), ViajeFactory
  (a700d36), nombres alineados al lenguaje ubicuo de HU-01 a HU-10 (f9b677e).
  Pasajero: agregado (cb6322d), value objects Correo y Telefono (ab741fb),
  RegistroPasajeroService y PasajeroFactory (1ed5e06).
- Arquitectura hexagonal (28 sep): puertos de viajes y pasajeros (e414851, ea940d1),
  adaptadores en memoria (ba994e9, 1a1859e), ViajeService (2454604), controladores
  y DTOs (94833d8, 094e11f).
- Persistencia y eventos (6–14 oct): H2 (54b0dff, 3bb882c), coordenadas, distancia
  recorrida y eventos de viaje (ab51a1c), reestructuración de paquetes (7f83cb7),
  colección de Bruno para probar la API (310800c), endpoint listarPasajeros (5360d47).
- Microservicios (20–21 nov): eureka-server (77c241d), gateway (a24d383), Kafka en
  KRaft en el docker-compose raíz (8118d19). pasajeros-service: proyecto con su base
  de datos (49a572a), mover el subdominio (6efd2d8), ExistePasajero por gRPC
  (48e31cf), registro en Eureka (0f30af4). viajes-service: proyecto con su base de
  datos (a420f4b), mover el subdominio (848b7a1), verificar al pasajero por gRPC con
  Circuit Breaker (82b52c0), consultar la ubicación del conductor por gRPC (41c2a4d),
  exponer ViajeGrpc (05c98d5), eventos con Transactional Outbox (2f0a3fc), registro
  en Eureka (4b52ed4).
- Frontends por actor en React + Vite: pasajero, conductor y administrador (88c1adf,
  9bcdffb, 6c3fc6f), integrados a develop con los PR #24, #25 y #26.
- Integración final (21–22 nov): corrección del tipo de documento del conductor
  (0a55a4b), formato del correo (4080d48), Docker para todos los servicios y sus
  bases de datos (75286bc), ajustes a emparejamiento (996aeb3).