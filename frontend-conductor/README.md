# frontend-conductor

App del conductor de Ride G3 (ride-hailing), en React + Vite. Cubre registro, disponibilidad y ubicación en tiempo real (HU-02), aceptar o rechazar ofertas (HU-04), iniciar y finalizar el viaje, y cancelación con motivo (HU-10). Incluye una simulación de recorrido para ver el seguimiento en vivo sin moverse de verdad.

## Cómo correrlo

Necesitas Node 22 LTS. Desde esta carpeta:

```powershell
npm install
npm run dev
```

Abre http://localhost:5174. Todas las llamadas a `/api` se reenvían al API Gateway en `http://localhost:8000` (ver `vite.config.js`), así que el Gateway, Eureka y los servicios deben estar corriendo.

Mientras los microservicios no estén todos afuera, puedes probarlo contra el monolito de `develop`, que atiende todas las rutas `/api` en el 8080:

```powershell
$env:API_TARGET = 'http://localhost:8080'; npm run dev
```

## Estructura

- `src/api.js`: los endpoints que usa esta app y qué microservicio atiende cada uno.
- `src/comun/`: cliente HTTP, utilidades y el mapa de OpenStreetMap (igual en los tres frontends).
- `src/componentes/`: las pantallas.
- `npm run build` genera `dist/`; `npm run lint` revisa el código con oxlint.
