import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import Mapa from '../comun/Mapa.jsx'
import {
  ESTADOS_VIAJE,
  esEstadoFinal,
  formatearDuracion,
  formatearFecha,
  formatearKm,
  idCorto,
  useSondeo,
} from '../comun/utilidades.js'
import Calificar from './Calificar.jsx'
import Cobro from './Cobro.jsx'

const PASOS = ['SOLICITADO', 'ASIGNADO', 'EN_CURSO', 'FINALIZADO']
const MENSAJES = {
  SOLICITADO: 'Buscando el conductor disponible más cercano…',
  ASIGNADO: 'Tu conductor va en camino al punto de origen.',
  EN_CURSO: 'Vas en camino a tu destino.',
  FINALIZADO: 'Llegaste a tu destino.',
  CANCELADO: 'Este viaje fue cancelado.',
}

// HU-05 (seguimiento en vivo), HU-06 (estado), HU-07 (tarifa), HU-08 (calificar), HU-10 (cancelar)
export default function ViajeActivo({ viajeId, pasajeroId, alCerrar }) {
  const [viaje, setViaje] = useState(null)
  const [error, setError] = useState('')
  const [seguimiento, setSeguimiento] = useState(null)
  const [avisoSeguimiento, setAvisoSeguimiento] = useState('')

  const final = viaje ? esEstadoFinal(viaje.estado) : false
  const enMovimiento = viaje?.estado === 'ASIGNADO' || viaje?.estado === 'EN_CURSO'

  // El estado cambia desde otros servicios (emparejamiento asigna, el conductor inicia y finaliza),
  // así que se consulta cada 3 s hasta que el viaje termina
  const recargar = async () => {
    try {
      setViaje(await api.consultarViaje(viajeId))
      setError('')
    } catch (e) {
      setError(e.message)
    }
  }
  useSondeo(recargar, 3000, !final)

  useSondeo(
    async () => {
      try {
        setSeguimiento(await api.ubicacionConductor(viajeId))
        setAvisoSeguimiento('')
      } catch (e) {
        setSeguimiento(null)
        setAvisoSeguimiento(e.message)
      }
    },
    3000,
    enMovimiento,
  )

  if (!viaje) {
    return (
      <section className="tarjeta">
        {error ? <Aviso>{error}</Aviso> : <p className="suave">Cargando el viaje…</p>}
        {error && (
          <button className="boton secundario" onClick={alCerrar}>
            Volver
          </button>
        )}
      </section>
    )
  }

  const origen = { lat: viaje.latitudOrigen, lon: viaje.longitudOrigen }
  const destino = { lat: viaje.latitudDestino, lon: viaje.longitudDestino }
  const conductor = enMovimiento && seguimiento ? { lat: seguimiento.latitud, lon: seguimiento.longitud } : null
  const objetivo = viaje.estado === 'ASIGNADO' ? origen : destino
  const puntos = [
    { id: 'origen', ...origen, color: '#16a34a', etiqueta: 'Origen' },
    { id: 'destino', ...destino, color: '#dc2626', etiqueta: 'Destino' },
    conductor && { id: 'conductor', ...conductor, color: '#2563eb', etiqueta: 'Conductor' },
  ].filter(Boolean)
  const indice = PASOS.indexOf(viaje.estado)

  return (
    <div className="pila">
      <section className="tarjeta">
        <div className="fila separada">
          <h2>Tu viaje</h2>
          <span className={`etiqueta ${ESTADOS_VIAJE[viaje.estado]?.clase ?? 'neutra'}`}>
            {ESTADOS_VIAJE[viaje.estado]?.texto ?? viaje.estado}
          </span>
        </div>
        {viaje.estado !== 'CANCELADO' && (
          <div className="pasos" aria-label="Progreso del viaje">
            {PASOS.map((paso, i) => (
              <div key={paso} className={`paso ${i <= indice ? 'hecho' : ''} ${i === indice ? 'actual' : ''}`}>
                {ESTADOS_VIAJE[paso].texto}
              </div>
            ))}
          </div>
        )}
        <p>{MENSAJES[viaje.estado]}</p>
        <p className="suave mono">id del viaje: {viaje.id}</p>
        <Aviso>{error}</Aviso>
      </section>

      <section className="tarjeta">
        <Mapa puntos={puntos} linea={conductor ? [conductor, objetivo] : undefined} />
        <div className="leyenda">
          <span><i style={{ background: '#16a34a' }} />Origen</span>
          <span><i style={{ background: '#dc2626' }} />Destino</span>
          {conductor && <span><i style={{ background: '#2563eb' }} />Conductor</span>}
        </div>
        {enMovimiento && seguimiento && (
          <div className="datos">
            <div className="dato">
              <span>{viaje.estado === 'ASIGNADO' ? 'Llega en' : 'Llegas en'}</span>
              <strong>{seguimiento.etaMinutos} min</strong>
            </div>
            <div className="dato">
              <span>Distancia restante</span>
              <strong>{formatearKm(seguimiento.distanciaRestanteKm)}</strong>
            </div>
          </div>
        )}
        {enMovimiento && avisoSeguimiento && <Aviso tipo="info">Ubicación del conductor: {avisoSeguimiento}</Aviso>}
        <div className="datos">
          <div className="dato"><span>Solicitado</span><strong>{formatearFecha(viaje.horaSolicitud)}</strong></div>
          <div className="dato"><span>Conductor</span><strong className="mono">{idCorto(viaje.conductorId)}</strong></div>
          {viaje.distanciaKm != null && (
            <div className="dato"><span>Distancia</span><strong>{formatearKm(viaje.distanciaKm)}</strong></div>
          )}
          {viaje.duracion != null && (
            <div className="dato"><span>Duración</span><strong>{formatearDuracion(viaje.duracion)}</strong></div>
          )}
        </div>
      </section>

      {!final && <Cancelar viaje={viaje} alCancelar={recargar} />}
      {viaje.estado === 'FINALIZADO' && <Cobro viajeId={viaje.id} tipo="tarifa" />}
      {viaje.estado === 'FINALIZADO' && <Calificar viajeId={viaje.id} pasajeroId={pasajeroId} />}
      {viaje.estado === 'CANCELADO' && <Cobro viajeId={viaje.id} tipo="cargo" />}
      {final && (
        <button className="boton ancho" onClick={alCerrar}>
          Pedir otro viaje
        </button>
      )}
    </div>
  )
}

// HU-10: cancelar con motivo. Después de la asignación puede generar un cargo parcial.
function Cancelar({ viaje, alCancelar }) {
  const [abierto, setAbierto] = useState(false)
  const [motivo, setMotivo] = useState('')
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const conCargo = viaje.estado === 'ASIGNADO' || viaje.estado === 'EN_CURSO'

  const cancelar = async (e) => {
    e.preventDefault()
    setEnviando(true)
    setError('')
    try {
      await api.cancelarViaje(viaje.id, motivo)
      setAbierto(false)
      await alCancelar()
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  if (!abierto) {
    return (
      <button className="boton peligro ancho" onClick={() => setAbierto(true)}>
        Cancelar viaje
      </button>
    )
  }
  return (
    <form className="tarjeta" onSubmit={cancelar}>
      <h3>Cancelar el viaje</h3>
      {conCargo && <Aviso tipo="info">Ya hay un conductor asignado: la cancelación puede generar un cargo parcial.</Aviso>}
      <label>
        Motivo
        <textarea value={motivo} onChange={(e) => setMotivo(e.target.value)} required />
      </label>
      <Aviso>{error}</Aviso>
      <div className="fila">
        <button className="boton peligro" disabled={enviando || !motivo.trim()}>
          {enviando ? 'Cancelando…' : 'Confirmar cancelación'}
        </button>
        <button type="button" className="boton secundario" onClick={() => setAbierto(false)}>
          Volver
        </button>
      </div>
    </form>
  )
}
