import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { ESTADOS_VIAJE, distanciaKm, esEstadoFinal, formatearDuracion, formatearKm, idCorto } from '../comun/utilidades.js'

// Viaje aceptado: iniciar al recoger al pasajero, finalizar al llegar, o cancelar con motivo (HU-10)
export default function ViajeConductor({ viaje, ubicacion, alCambiar, alTerminar }) {
  const [motivo, setMotivo] = useState('')
  const [cancelando, setCancelando] = useState(false)
  const [error, setError] = useState('')
  const [ocupado, setOcupado] = useState(false)

  if (!viaje) {
    return (
      <section className="tarjeta">
        <p className="suave">Cargando el viaje…</p>
      </section>
    )
  }

  const ejecutar = async (accion) => {
    setOcupado(true)
    setError('')
    try {
      await accion()
      alCambiar(await api.consultarViaje(viaje.id))
      setCancelando(false)
    } catch (e) {
      setError(e.message)
    } finally {
      setOcupado(false)
    }
  }

  const origen = { lat: viaje.latitudOrigen, lon: viaje.longitudOrigen }
  const destino = { lat: viaje.latitudDestino, lon: viaje.longitudDestino }
  const objetivo = viaje.estado === 'ASIGNADO' ? origen : destino
  const estado = ESTADOS_VIAJE[viaje.estado] ?? { texto: viaje.estado, clase: 'neutra' }

  return (
    <section className="tarjeta">
      <div className="fila separada">
        <h2>Viaje {idCorto(viaje.id)}</h2>
        <span className={`etiqueta ${estado.clase}`}>{estado.texto}</span>
      </div>

      {!esEstadoFinal(viaje.estado) && (
        <div className="datos">
          <div className="dato">
            <span>{viaje.estado === 'ASIGNADO' ? 'Hasta el origen' : 'Hasta el destino'}</span>
            <strong>{formatearKm(distanciaKm(ubicacion, objetivo))}</strong>
          </div>
          <div className="dato">
            <span>Pasajero</span>
            <strong className="mono">{idCorto(viaje.pasajeroId)}</strong>
          </div>
        </div>
      )}

      {viaje.estado === 'ASIGNADO' && (
        <button className="boton ancho" disabled={ocupado} onClick={() => ejecutar(() => api.iniciarViaje(viaje.id))}>
          Recogí al pasajero: iniciar viaje
        </button>
      )}
      {viaje.estado === 'EN_CURSO' && (
        <button className="boton ancho" disabled={ocupado} onClick={() => ejecutar(() => api.finalizarViaje(viaje.id))}>
          Llegamos: finalizar viaje
        </button>
      )}

      {viaje.estado === 'FINALIZADO' && (
        <div className="datos">
          <div className="dato"><span>Distancia</span><strong>{formatearKm(viaje.distanciaKm)}</strong></div>
          <div className="dato"><span>Duración</span><strong>{formatearDuracion(viaje.duracion)}</strong></div>
        </div>
      )}
      {viaje.estado === 'CANCELADO' && <p className="suave">El viaje fue cancelado.</p>}

      {!esEstadoFinal(viaje.estado) &&
        (cancelando ? (
          <form
            className="pila"
            style={{ gap: 10 }}
            onSubmit={(e) => {
              e.preventDefault()
              ejecutar(() => api.cancelarViaje(viaje.id, motivo))
            }}
          >
            <label>
              Motivo de la cancelación
              <textarea value={motivo} onChange={(e) => setMotivo(e.target.value)} required />
            </label>
            <div className="fila">
              <button className="boton peligro" disabled={ocupado || !motivo.trim()}>
                Confirmar cancelación
              </button>
              <button type="button" className="boton secundario" onClick={() => setCancelando(false)}>
                Volver
              </button>
            </div>
          </form>
        ) : (
          <button className="boton peligro" onClick={() => setCancelando(true)}>
            Cancelar viaje
          </button>
        ))}

      <Aviso>{error}</Aviso>
      {esEstadoFinal(viaje.estado) && (
        <button className="boton ancho" onClick={alTerminar}>
          Volver a recibir ofertas
        </button>
      )}
    </section>
  )
}
