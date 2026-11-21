import { solicitar } from './comun/http.js'

// Endpoints que usa el conductor. Cada línea dice qué microservicio la atiende detrás del Gateway.
export const api = {
  // conductores-service
  registrarConductor: (datos) => solicitar('POST', '/api/conductores', datos),
  consultarConductor: (id) => solicitar('GET', `/api/conductores/${id}`),
  conectarse: (id, ubicacion) => solicitar('PUT', `/api/conductores/${id}/disponibilidad`, ubicacion),
  actualizarUbicacion: (id, ubicacion) => solicitar('PUT', `/api/conductores/${id}/ubicacion`, ubicacion),
  desconectarse: (id) => solicitar('PUT', `/api/conductores/${id}/desactivar`),

  // emparejamiento-service
  // /conductor/{id} todavía no existe en el backend: está propuesto en la guía. Mientras no exista,
  // la pantalla de ofertas cae a buscar por id de viaje con /viaje/{viajeId}.
  ofertasDelConductor: (id) => solicitar('GET', `/api/ofertas/conductor/${id}`),
  ofertasDelViaje: (viajeId) => solicitar('GET', `/api/ofertas/viaje/${viajeId}`),
  aceptarOferta: (id) => solicitar('POST', `/api/ofertas/${id}/aceptar`),
  rechazarOferta: (id) => solicitar('POST', `/api/ofertas/${id}/rechazar`),

  // viajes-service
  consultarViaje: (id) => solicitar('GET', `/api/viajes/${id}`),
  iniciarViaje: (id) => solicitar('POST', `/api/viajes/${id}/iniciar`),
  finalizarViaje: (id) => solicitar('POST', `/api/viajes/${id}/finalizar`),
  cancelarViaje: (id, motivo) => solicitar('POST', `/api/viajes/${id}/cancelar`, motivo, 'texto'),
}
