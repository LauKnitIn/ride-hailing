# Matriz de decisión ponderada

## Alternativas

- **A — Monolito modular:** todas las capacidades se despliegan como una
  aplicación; los módulos conservan fronteras internas.
- **B — Microservicios completos:** cada capacidad se despliega y opera como un
  servicio independiente.
- **C — Híbrida actual:** se conserva `ride_hailing/` modular y coexiste con
  servicios independientes detrás de Gateway y Eureka.

Escala: 1 = muy desfavorable para el criterio; 5 = muy favorable. El resultado
por celda es `puntaje × peso`.

## Criterios y pesos

| Criterio | Peso | Justificación |
|---|---:|---|
| Modificabilidad | 5 | La arquitectura debe permitir evolucionar capacidades sin reescribir todo el producto. |
| Integridad | 5 | Los cambios de viaje y sus eventos deben conservar consistencia, incluso con fallos de Kafka. |
| Testabilidad | 4 | El equipo debe validar cambios con pruebas de módulo y contratos. |
| Interoperabilidad | 3 | REST/gRPC/Kafka son necesarios, pero no todas las capacidades requieren todos los mecanismos. |
| Simplicidad operativa | 5 | El equipo es pequeño y el plazo académico no permite una plataforma compleja. |
| Viabilidad en equipo y plazo | 4 | La opción debe aprovechar lo construido y poder terminarse dentro del curso. |
| Autonomía de despliegue | 4 | Es útil separar cambios que evolucionan a ritmos distintos, sin asumir que todos necesitan servicio propio. |

## Puntuación ponderada

| Criterio | Peso | A: puntaje → total | B: puntaje → total | C: puntaje → total |
|---|---:|---:|---:|---:|
| Modificabilidad | 5 | 3 → 15 | 5 → 25 | 4 → 20 |
| Integridad | 5 | 4 → 20 | 3 → 15 | 4 → 20 |
| Testabilidad | 4 | 4 → 16 | 3 → 12 | 4 → 16 |
| Interoperabilidad | 3 | 2 → 6 | 5 → 15 | 4 → 12 |
| Simplicidad operativa | 5 | 5 → 25 | 1 → 5 | 3 → 15 |
| Viabilidad en equipo y plazo | 4 | 5 → 20 | 2 → 8 | 4 → 16 |
| Autonomía de despliegue | 4 | 2 → 8 | 5 → 20 | 4 → 16 |
| **Total** | **30** | **110** | **100** | **115** |

## Análisis de sensibilidad

Se varía cada uno de los dos criterios de peso 5 por separado en ±2; los demás
pesos permanecen constantes. Modificabilidad y simplicidad operativa son los
dos criterios prioritarios porque expresan tanto la capacidad de evolución
como la restricción del equipo.

| Variación | A: monolito | B: microservicios | C: híbrida | Ganador |
|---|---:|---:|---:|---|
| Base | 110 | 100 | 115 | C |
| Modificabilidad 5 → 3 | 104 | 90 | 107 | C |
| Modificabilidad 5 → 7 | 116 | 110 | 123 | C |
| Simplicidad operativa 5 → 3 | 100 | 98 | 109 | C |
| Simplicidad operativa 5 → 7 | 120 | 102 | 121 | C |

El ganador se mantiene en los cambios individuales, aunque C supera a A por un
margen estrecho en la variación de simplicidad operativa. Los puntajes son
juicios del equipo, no mediciones empíricas; la cercanía sugiere que conviene
medir el costo real de mantener los servicios y revisar la matriz al terminar
un ciclo de integración.

## Decisión

Elegimos **C, la arquitectura híbrida**, porque obtiene la mayor puntuación
(115) y conserva la evolución gradual sin exigir que todas las capacidades sean
microservicios desde el comienzo. Aceptamos el costo de operar varias bases,
contratos y despliegues. Si la carga operativa real supera lo que el equipo puede
mantener, reevaluaríamos qué servicios deben seguir separados y cuáles pueden
permanecer como módulos.
