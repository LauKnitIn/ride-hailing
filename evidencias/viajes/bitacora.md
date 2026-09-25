# Bitácora — Dev 2 · Subdominio "Viajes y seguimiento" · G3

| Paso | Tiempo estimado | Tiempo real | Commit(s) |
|---|---|---|---|
| 1 · Lenguaje Ubicuo (auditar nombres de clases/campos/métodos vs. HU) | ~20 min | 10 min | refactor: ajusta nombres al lenguaje ubicuo de HU-01 a HU-10 |
| 2 · Value Object (`EstadoViaje`) | ~45 min | 30 min | "feat: value object EstadoViaje con transiciones válidas" | |
| 3 · Servicio de Dominio (`TransicionEstadoViajeService`) | ~35 min | |  |
| 4 · Límite del Agregado (documentar y respetar cómo `Viaje` referencia otras entidades) | ~15 min | 5 min | feat: raíz del agregado Viaje, referencia a Conductor y Pasajero solo por id |
| 5 · Factory (`ViajeFactory`) | ~35 min | |  |
| 6 · Commit y push + Pull Request | ~5 min | |  |
| **Total** | **~2 h 35 min** | | | |

## Notas del paso 1 · Lenguaje Ubicuo

Términos auditados en el subdominio "Viajes y seguimiento" contra `Requisitos_G3.docx`:

- `Viaje` (raíz del Agregado) — no confundir con "solicitud" ni "carrera"; en las HU siempre se usa "viaje".
- `EstadoViaje` — usa exactamente los nombres de HU-06: `solicitado`, `asignado`, `en curso`, `finalizado`, `cancelado`.
- `pasajeroId` / `conductorId` — deliberadamente ids, no objetos, porque `Pasajero` y `Conductor` no son parte de este subdominio.
