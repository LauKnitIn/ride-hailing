import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// Todas las llamadas a /api se reenvían al API Gateway (puerto 8000), que las enruta
// al microservicio correspondiente usando Eureka. Así el navegador solo conoce un origen
// y no hace falta configurar CORS en los servicios.
//
// Para probar contra el monolito de develop (que atiende todas las rutas /api en el 8080):
//   PowerShell:  $env:API_TARGET = 'http://localhost:8080'; npm run dev
const destino = process.env.API_TARGET ?? 'http://localhost:8000'
const proxy = { '/api': { target: destino, changeOrigin: true } }

export default defineConfig({
  plugins: [react()],
  server: { port: 5173, strictPort: true, proxy },
  preview: { port: 5173, strictPort: true, proxy },
})
