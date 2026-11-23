# Caso real: Shopify y la evolución hacia un monolito modular

## Contexto

Shopify describe una aplicación Ruby on Rails de más de una década, desarrollada
por más de mil personas y con alrededor de seis mil clases. Sus áreas de negocio
se encontraban muy acopladas: un cambio en una regla podía generar fallos de
pruebas en áreas aparentemente no relacionadas y los nuevos integrantes debían
aprender demasiado contexto antes de modificar una capacidad.

## Decisión tomada

En vez de dividir inmediatamente el producto en microservicios, Shopify eligió
reorganizar el código por componentes de negocio dentro de un mismo despliegue.
Cada componente debía tener una API pública y ser dueño de sus datos. El equipo
automatizó la reorganización inicial y desarrolló Wedge para detectar llamadas
que cruzaban límites de componentes sin usar interfaces públicas.

## Resultado

El artículo no publica una métrica cuantitativa de mejora. Sí reporta beneficios
cualitativos: el trabajo de aislamiento ayudó a sustituir un motor de impuestos
heredado por uno nuevo. El artículo también reconoce que el aislamiento completo
es un trabajo gradual y que la estrategia se adapta a la escala y al equipo.

## Relación con Ride Hailing

La lección aplicable no es copiar la tecnología ni el tamaño de Shopify, sino
mantener límites explícitos y elegir el grado de distribución según necesidades
demostradas. Ride Hailing ya combina `ride_hailing/` con servicios independientes,
Gateway, Eureka y bases por servicio. Por eso el caso se aplica **parcialmente**:

- Sí aplica la recomendación de definir APIs públicas, propiedad de datos y
  pruebas que detecten dependencias indebidas.
- Sí aplica evitar nuevos microservicios por moda y evaluar su costo operativo.
- No prueba que debamos volver a un monolito: nuestro proyecto ya tiene flujos
  entre servicios y la decisión debe considerar esos contratos existentes.

Para adoptar mejor esta lección, el equipo tendría que mantener claro qué
implementación es la activa, impedir que el monolito y los servicios dupliquen
reglas y comprobar las fronteras con pruebas automáticas.

## Referencia IEEE

[1] K. Westeinde, “Deconstructing the Monolith: Designing Software that
Maximizes Developer Productivity,” *Shopify Engineering*, 21 de febrero de
2019. [En línea]. Disponible:
https://shopify.engineering/deconstructing-monolith-designing-software-maximizes-developer-productivity.
[Consultado: 22 de noviembre de 2026].
