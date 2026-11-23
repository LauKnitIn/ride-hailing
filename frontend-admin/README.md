# frontend-admin

Panel del administrador de Ride G3 (ride-hailing), en React + Vite. Muestra qué microservicios responden a través del Gateway, los pasajeros con su historial, los conductores disponibles y el detalle de cualquier viaje con sus ofertas y tarifas, más acciones de soporte (asignar conductor, rechazar por falta de conductores, recalcular la tarifa).

## Cómo correrlo

Necesitas Node 22 LTS. Desde esta carpeta:

```powershell
npm install
npm run dev
```

Abre http://localhost:5175. Todas las llamadas a `/api` se reenvían al API Gateway en `http://localhost:8000` (ver `vite.config.js`), así que el Gateway, Eureka y los servicios deben estar corriendo.

Mientras los microservicios no estén todos afuera, puedes probarlo contra el monolito de `develop`, que atiende todas las rutas `/api` en el 8080:

```powershell
$env:API_TARGET = 'http://localhost:8080'; npm run dev
```

## Estructura

- `src/api.js`: los endpoints que usa esta app y qué microservicio atiende cada uno.
- `src/comun/`: cliente HTTP, utilidades (igual en los tres frontends).
- `src/componentes/`: las pantallas.
- `npm run build` genera `dist/`; `npm run lint` revisa el código con oxlint.
