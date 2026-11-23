import { solicitar } from './comun/http.js'

// Endpoints que usa el administrador. Cada línea dice qué microservicio la atiende detrás del Gateway.
export const api = {
  // pasajeros-service
  listarPasajeros: () => solicitar('GET', '/api/pasajeros'),

  // conductores-service
  conductoresDisponibles: () => solicitar('GET', '/api/conductores/disponibles'),
  consultarConductor: (id) => solicitar('GET', `/api/conductores/${id}`),
  desconectarConductor: (id) => solicitar('PUT', `/api/conductores/${id}/desactivar`),

  // viajes-service
  consultarViaje: (id) => solicitar('GET', `/api/viajes/${id}`),
  historial: (pasajeroId) => solicitar('GET', `/api/viajes/pasajero/${pasajeroId}`),
  asignarConductor: (viajeId, conductorId) => solicitar('POST', `/api/viajes/${viajeId}/conductor/${conductorId}`),
  rechazarSinConductores: (viajeId, motivo) =>
    solicitar('POST', `/api/viajes/${viajeId}/rechazar-sin-conductores`, motivo, 'texto'),

  // emparejamiento-service
  ofertasDelViaje: (viajeId) => solicitar('GET', `/api/ofertas/viaje/${viajeId}`),

  // tarifas-service
  tarifa: (viajeId) => solicitar('GET', `/api/tarifas/${viajeId}`),
  cargoParcial: (viajeId) => solicitar('GET', `/api/tarifas/${viajeId}/cargo-parcial`),
  calcularTarifa: (viajeId) => solicitar('POST', `/api/tarifas/${viajeId}/calcular`),
}

// Una ruta barata por servicio para saber si el Gateway lo alcanza. Cualquier respuesta del servicio,
// incluso 404 o 405, significa que está registrado en Eureka y vivo; 502/503/504 significa que no.
const CEROS = '00000000-0000-0000-0000-000000000000'
export const SONDAS = [
  { servicio: 'pasajeros-service', ruta: '/api/pasajeros' },
  { servicio: 'viajes-service', ruta: `/api/viajes/${CEROS}` },
  { servicio: 'conductores-service', ruta: '/api/conductores/disponibles' },
  { servicio: 'emparejamiento-service', ruta: `/api/ofertas/viaje/${CEROS}` },
  { servicio: 'tarifas-service', ruta: `/api/tarifas/${CEROS}` },
  { servicio: 'calificacion-service', ruta: '/api/calificaciones' },
]

export async function sondear(ruta) {
  const inicio = performance.now()
  try {
    const respuesta = await fetch(ruta)
    const ms = Math.round(performance.now() - inicio)
    return { vivo: ![502, 503, 504].includes(respuesta.status), status: respuesta.status, ms }
  } catch {
    return { vivo: false, status: null, ms: null }
  }
}
