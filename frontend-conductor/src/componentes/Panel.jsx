import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import Mapa from '../comun/Mapa.jsx'
import { CENTRO_TUNJA, distanciaKm, esEstadoFinal, guardar, leer, useSondeo } from '../comun/utilidades.js'
import Ofertas from './Ofertas.jsx'
import ViajeConductor from './ViajeConductor.jsx'

const DISPONIBILIDAD = {
  INACTIVO: { texto: 'Desconectado', clase: 'neutra' },
  DISPONIBLE: { texto: 'Disponible', clase: 'bien' },
  EN_VIAJE: { texto: 'En viaje', clase: 'aviso-e' },
}

// HU-02 (disponibilidad y ubicación en tiempo real), HU-04 (aceptar o rechazar), ciclo del viaje
export default function Panel({ conductor, alActualizar }) {
  const [ubicacion, setUbicacion] = useState(() =>
    conductor.latitud != null ? { lat: conductor.latitud, lon: conductor.longitud } : CENTRO_TUNJA,
  )
  const [viajeId, setViajeId] = useState(() => leer('rh.viajeConductor'))
  const [viaje, setViaje] = useState(null)
  const [simular, setSimular] = useState(false)
  const [error, setError] = useState('')
  const [ocupado, setOcupado] = useState(false)

  const conectado = conductor.disponibilidad === 'DISPONIBLE' || conductor.disponibilidad === 'EN_VIAJE'

  // La disponibilidad también cambia desde viajes (por Kafka): se refresca cada 5 s
  useSondeo(async () => {
    try {
      alActualizar(await api.consultarConductor(conductor.id))
    } catch (e) {
      setError(e.message)
    }
  }, 5000)

  // Viaje aceptado: se consulta cada 3 s hasta que termina
  useSondeo(
    async () => {
      try {
        setViaje(await api.consultarViaje(viajeId))
      } catch (e) {
        setError(e.message)
      }
    },
    3000,
    Boolean(viajeId) && !(viaje && esEstadoFinal(viaje.estado)),
  )

  const objetivo =
    viaje?.estado === 'ASIGNADO'
      ? { lat: viaje.latitudOrigen, lon: viaje.longitudOrigen }
      : viaje?.estado === 'EN_CURSO'
        ? { lat: viaje.latitudDestino, lon: viaje.longitudDestino }
        : null

  const moverA = async (punto) => {
    setUbicacion(punto)
    if (!conectado) return
    try {
      await api.actualizarUbicacion(conductor.id, { latitud: punto.lat, longitud: punto.lon })
      setError('')
    } catch (e) {
      setError(e.message)
    }
  }

  // Simulación de recorrido: cada 3 s avanza un tramo hacia el origen (asignado) o el destino (en curso),
  // para poder ver el seguimiento en vivo (HU-05) en la app del pasajero sin moverse de verdad
  useSondeo(
    () => {
      if (!objetivo) return
      const restante = distanciaKm(ubicacion, objetivo)
      const fraccion = restante < 0.05 ? 1 : Math.min(1, 0.25 / restante)
      moverA({
        lat: ubicacion.lat + (objetivo.lat - ubicacion.lat) * fraccion,
        lon: ubicacion.lon + (objetivo.lon - ubicacion.lon) * fraccion,
      })
    },
    3000,
    simular && Boolean(objetivo),
  )

  const conmutarConexion = async () => {
    setOcupado(true)
    setError('')
    try {
      const actualizado = conectado
        ? await api.desconectarse(conductor.id)
        : await api.conectarse(conductor.id, { latitud: ubicacion.lat, longitud: ubicacion.lon })
      alActualizar(actualizado)
    } catch (e) {
      setError(e.message)
    } finally {
      setOcupado(false)
    }
  }

  const usarGps = () => {
    if (!navigator.geolocation) {
      setError('Este navegador no permite leer la ubicación.')
      return
    }
    navigator.geolocation.getCurrentPosition(
      (p) => moverA({ lat: p.coords.latitude, lon: p.coords.longitude }),
      () => setError('No se pudo leer la ubicación del dispositivo.'),
    )
  }

  const tomarViaje = (id) => {
    guardar('rh.viajeConductor', id)
    setViaje(null)
    setViajeId(id)
    // Al aceptar, el conductor pasa a "en viaje": se refresca ya en vez de esperar el siguiente sondeo
    api.consultarConductor(conductor.id).then(alActualizar).catch(() => {})
  }
  const soltarViaje = () => {
    guardar('rh.viajeConductor', null)
    setViajeId(null)
    setViaje(null)
    setSimular(false)
  }

  const estado = DISPONIBILIDAD[conductor.disponibilidad] ?? { texto: conductor.disponibilidad, clase: 'neutra' }
  const puntos = [
    { id: 'yo', ...ubicacion, color: '#c2410c', etiqueta: 'Tú' },
    viaje && !esEstadoFinal(viaje.estado) && { id: 'origen', lat: viaje.latitudOrigen, lon: viaje.longitudOrigen, color: '#16a34a', etiqueta: 'Origen' },
    viaje && !esEstadoFinal(viaje.estado) && { id: 'destino', lat: viaje.latitudDestino, lon: viaje.longitudDestino, color: '#dc2626', etiqueta: 'Destino' },
  ].filter(Boolean)

  return (
    <div className="rejilla-2" style={{ alignItems: 'start' }}>
      <section className="tarjeta">
        <div className="fila separada">
          <h2>Mi estado</h2>
          <span className={`etiqueta ${estado.clase}`}>{estado.texto}</span>
        </div>
        <Mapa puntos={puntos} linea={objetivo ? [ubicacion, objetivo] : undefined} alHacerClic={(lat, lon) => moverA({ lat, lon })} />
        <p className="suave">
          Toca el mapa para mover tu ubicación{conectado ? '; se envía a conductores-service al instante' : ''}.
        </p>
        <div className="fila">
          <button className="boton" onClick={conmutarConexion} disabled={ocupado || conductor.disponibilidad === 'EN_VIAJE'}>
            {conectado ? 'Desconectarme' : 'Conectarme'}
          </button>
          <button className="boton secundario" onClick={usarGps}>
            Usar mi ubicación
          </button>
        </div>
        {conductor.disponibilidad === 'EN_VIAJE' && (
          <p className="suave">No puedes desconectarte con un viaje en curso.</p>
        )}
        {objetivo && (
          <label className="fila" style={{ display: 'flex', fontWeight: 600 }}>
            <input type="checkbox" checked={simular} onChange={(e) => setSimular(e.target.checked)} style={{ width: 'auto' }} />
            Simular el recorrido hacia {viaje.estado === 'ASIGNADO' ? 'el origen' : 'el destino'}
          </label>
        )}
        <p className="suave mono">id: {conductor.id}</p>
        <Aviso>{error}</Aviso>
      </section>

      {viajeId ? (
        <ViajeConductor viaje={viaje} ubicacion={ubicacion} alCambiar={setViaje} alTerminar={soltarViaje} />
      ) : (
        <Ofertas conductor={conductor} disponible={conductor.disponibilidad === 'DISPONIBLE'} alAceptar={tomarViaje} />
      )}
    </div>
  )
}
