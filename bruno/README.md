# Coleccion Bruno - Ride Hailing API

1. Arranca la app: `./mvnw spring-boot:run` (puerto 8080).
2. En Bruno: **Open Collection** y elige esta carpeta `bruno/`.
3. Selecciona el entorno **Local** (variable `baseUrl`).
4. Ejecuta las carpetas **en orden** (clic derecho en la carpeta, **Run**). Los ids se
   guardan solos entre peticiones (`pasajeroId`, `conductorId`, `viajeId`, `ofertaId`...).

| Carpeta | Que cubre |
|---|---|
| `1 Flujo completo` | Camino feliz HU-01 a HU-09: registro, disponibilidad, solicitud, oferta, aceptacion, seguimiento, viaje, tarifa automatica, calificacion, historial. |
| `2 Flujos alternativos` | Cancelar con cargo parcial (HU-10), rechazar oferta, rechazar sin conductores, asignacion directa, desactivar conductor. |
| `3 Casos de error` | 404, 400, 403 y 409 esperados. |

Notas:
- La carpeta 2 y 3 dependen de `pasajeroId`, `conductorId` y `ts` creados en la carpeta 1.
- Las bases H2 son en memoria: al reiniciar la app hay que volver a correr la carpeta 1.
- Cada ejecucion genera correo y documento unicos (`ts`), asi que se puede repetir sin reiniciar.
- Los endpoints cubiertos son los 25 de la API (pasajeros, conductores, viajes, ofertas, tarifas, calificaciones).
