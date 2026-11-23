import { useEffect, useRef } from 'react'

// --- Almacenamiento local: solo para recordar quién usa la app en este navegador ---

export function leer(clave) {
  try {
    const valor = localStorage.getItem(clave)
    return valor ? JSON.parse(valor) : null
  } catch {
    return null
  }
}

export function guardar(clave, valor) {
  try {
    if (valor == null) localStorage.removeItem(clave)
    else localStorage.setItem(clave, JSON.stringify(valor))
  } catch {
    // Navegador sin almacenamiento (modo privado): la app sigue funcionando sin recordar
  }
}

// --- Sondeo periódico: ejecuta `accion` ya y luego cada `intervaloMs` mientras `activo` ---

export function useSondeo(accion, intervaloMs, activo = true) {
  const ultima = useRef(accion)
  useEffect(() => {
    ultima.current = accion
  })
  useEffect(() => {
    if (!activo) return undefined
    let vigente = true
    const ejecutar = () => {
      if (vigente) ultima.current()
    }
    ejecutar()
    const id = setInterval(ejecutar, intervaloMs)
    return () => {
      vigente = false
      clearInterval(id)
    }
  }, [intervaloMs, activo])
}

// --- Formatos ---

const pesos = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })

export const formatearPesos = (valor) => pesos.format(valor)

export function formatearFecha(valor) {
  if (valor == null) return '—'
  // Jackson puede enviar un Instant como texto ISO o como segundos desde 1970
  const fecha = typeof valor === 'number' ? new Date(valor * 1000) : new Date(valor)
  return Number.isNaN(fecha.getTime())
    ? String(valor)
    : fecha.toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' })
}

export function formatearDuracion(valor) {
  if (valor == null) return '—'
  let segundos
  if (typeof valor === 'number') {
    segundos = valor
  } else {
    // Duración ISO-8601, por ejemplo PT12M30.5S
    const partes = /PT(?:(\d+(?:\.\d+)?)H)?(?:(\d+(?:\.\d+)?)M)?(?:(\d+(?:\.\d+)?)S)?/.exec(valor)
    if (!partes) return String(valor)
    segundos = Number(partes[1] ?? 0) * 3600 + Number(partes[2] ?? 0) * 60 + Number(partes[3] ?? 0)
  }
  const minutos = Math.round(segundos / 60)
  return minutos < 1 ? 'menos de 1 min' : `${minutos} min`
}

export const formatearKm = (km) => (km == null ? '—' : `${km.toFixed(km < 10 ? 2 : 1)} km`)

export const idCorto = (id) => (id ? String(id).slice(0, 8) : '—')

// --- Estados del viaje (HU-06) ---

export const ESTADOS_VIAJE = {
  SOLICITADO: { texto: 'Solicitado', clase: 'neutra' },
  ASIGNADO: { texto: 'Conductor asignado', clase: 'aviso-e' },
  EN_CURSO: { texto: 'En curso', clase: 'bien' },
  FINALIZADO: { texto: 'Finalizado', clase: 'bien' },
  CANCELADO: { texto: 'Cancelado', clase: 'mal' },
}

export const esEstadoFinal = (estado) => estado === 'FINALIZADO' || estado === 'CANCELADO'

// Distancia en línea recta (Haversine), igual que Ubicacion.distanciaHaciaKm del backend
export function distanciaKm(a, b) {
  const rad = (g) => (g * Math.PI) / 180
  const dLat = rad(b.lat - a.lat)
  const dLon = rad(b.lon - a.lon)
  const h = Math.sin(dLat / 2) ** 2 + Math.cos(rad(a.lat)) * Math.cos(rad(b.lat)) * Math.sin(dLon / 2) ** 2
  return 6371 * 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h))
}

// Centro por defecto de los mapas: Tunja
export const CENTRO_TUNJA = { lat: 5.5353, lon: -73.3678 }
