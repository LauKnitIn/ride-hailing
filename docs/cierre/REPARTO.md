# Reparto de trabajo

## Integrantes

- Jorge Gonzalez
- Laura Correa
- Bryan Torres

## Alcance

Este reparto registra quién redactó cada entregable de la Lección 13 y quién lo
revisó. El código de los servicios se repartió por módulos (ver más abajo).

| Entregable | Responsable de redacción inicial | Revisión |
|---|---|---|
| `ARQUITECTURA.md` | Jorge Gonzalez | Laura Correa y Bryan Torres |
| `ATAM-LITE.md` | Laura Correa | Jorge Gonzalez y Bryan Torres |
| `MATRIZ.md` | Laura Correa | Jorge Gonzalez y Bryan Torres |
| `CASO.md` | Laura Correa | Bryan Torres |
| `GOBERNANZA.md` | Laura Correa | Jorge Gonzalez |
| `RESUMEN-EJECUTIVO.md` | Laura Correa | Prueba de lectura del equipo |
| `RETROSPECTIVA-Jorge-Gonzalez.md` | Jorge Gonzalez | Individual |
| `RETROSPECTIVA - LAURA CORREA.md` | Laura Correa | Individual |
| `RETROSPECTIVA-Bryan-Torres.md` | Bryan Torres | Individual |

## Aporte técnico por integrante

Los commits por autor se pueden consultar con `git shortlog -sn --no-merges`.

- **Bryan Torres:** documentación de arquitectura y diagrama C4, persistencia
  PostgreSQL y migración Flyway de Calificaciones, consulta de calificaciones por
  conductor, organización del monolito modular.
- **Jorge Gonzalez:** servicios de dominio (viajes, conductores, emparejamiento),
  comunicación gRPC/Kafka y Outbox, pruebas.
- **Laura Correa:** frontends, integración, documentos de cierre y retrospectiva,
  coordinación de ramas y pull requests.

## Evidencia consultada

- `de53745`: documentación de arquitectura y diagrama C4.
- `6d4acfb`: persistencia PostgreSQL y migración Flyway de Calificaciones.
- `c95312c`: consulta de calificaciones por conductor.
- `dcb167a`: organización del monolito modular dentro de `ride_hailing/`.
