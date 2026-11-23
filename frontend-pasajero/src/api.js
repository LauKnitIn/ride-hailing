import { solicitar } from './comun/http.js'

// Endpoints que usa el pasajero. Cada línea dice qué microservicio la atiende detrás del Gateway.
export const api = {
  // pasajeros-service
  registrarPasajero: (datos) => solicitar('POST', '/api/pasajeros', datos),
  listarPasajeros: () => solicitar('GET', '/api/pasajeros'),
  consultarPasajero: (id) => solicitar('GET', `/api/pasajeros/${id}`),
  actualizarPerfil: (id, datos) => solicitar('PUT', `/api/pasajeros/${id}/perfil`, datos),

  // viajes-service
  solicitarViaje: (datos) => solicitar('POST', '/api/viajes', datos),
  consultarViaje: (id) => solicitar('GET', `/api/viajes/${id}`),
  ubicacionConductor: (id) => solicitar('GET', `/api/viajes/${id}/ubicacion-conductor`),
  cancelarViaje: (id, motivo) => solicitar('POST', `/api/viajes/${id}/cancelar`, motivo, 'texto'),
  historial: (pasajeroId) => solicitar('GET', `/api/viajes/pasajero/${pasajeroId}`),

  // tarifas-service
  tarifa: (viajeId) => solicitar('GET', `/api/tarifas/${viajeId}`),
  cargoParcial: (viajeId) => solicitar('GET', `/api/tarifas/${viajeId}/cargo-parcial`),

  // calificacion-service
  calificar: (datos) => solicitar('POST', '/api/calificaciones', datos),
}
