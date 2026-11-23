import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { ESTADOS_VIAJE, formatearDuracion, formatearFecha, formatearKm, formatearPesos } from '../comun/utilidades.js'

// Detalle de un viaje con datos de tres servicios (viajes, emparejamiento, tarifas) y acciones de soporte
export default function Viajes({ viajeInicial }) {
  const [idBuscado, setIdBuscado] = useState(viajeInicial ?? '')
  const [viaje, setViaje] = useState(null)
  const [ofertas, setOfertas] = useState([])
  const [cobros, setCobros] = useState({ tarifa: null, cargo: null })
  const [error, setError] = useState('')

  const cargar = useCallback(async (id) => {
    try {
      const encontrado = await api.consultarViaje(id)
      setError('')
      setViaje(encontrado)
      // Los demás datos vienen de otros servicios: si alguno no responde, el detalle se muestra igual
      const [o, t, c] = await Promise.allSettled([api.ofertasDelViaje(id), api.tarifa(id), api.cargoParcial(id)])
      setOfertas(o.status === 'fulfilled' ? o.value : [])
      setCobros({
        tarifa: t.status === 'fulfilled' ? t.value.monto : null,
        cargo: c.status === 'fulfilled' ? c.value.monto : null,
      })
    } catch (e) {
      setViaje(null)
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    // cargar es asíncrona: actualiza el estado después de la respuesta, no durante el efecto
    // eslint-disable-next-line react/set-state-in-effect
    if (viajeInicial) cargar(viajeInicial)
  }, [viajeInicial, cargar])

  return (
    <div className="pila">
      <form
        className="tarjeta"
        onSubmit={(e) => {
          e.preventDefault()
          cargar(idBuscado.trim())
        }}
      >
        <h2>Buscar viaje</h2>
        <div className="fila">
          <input aria-label="Id del viaje" placeholder="Id del viaje" value={idBuscado} onChange={(e) => setIdBuscado(e.target.value)} style={{ flex: 1 }} />
          <button className="boton" disabled={!idBuscado.trim()}>Buscar</button>
        </div>
        <Aviso>{error}</Aviso>
      </form>

      {viaje && (
        <>
          <section className="tarjeta">
            <div className="fila separada">
              <h2 className="mono">{viaje.id}</h2>
              <span className={`etiqueta ${ESTADOS_VIAJE[viaje.estado]?.clase ?? 'neutra'}`}>
                {ESTADOS_VIAJE[viaje.estado]?.texto ?? viaje.estado}
              </span>
            </div>
            <div className="datos">
              <Dato titulo="Pasajero" valor={viaje.pasajeroId} mono />
              <Dato titulo="Conductor" valor={viaje.conductorId ?? '—'} mono />
              <Dato titulo="Origen" valor={`${viaje.latitudOrigen}, ${viaje.longitudOrigen}`} />
              <Dato titulo="Destino" valor={`${viaje.latitudDestino}, ${viaje.longitudDestino}`} />
              <Dato titulo="Solicitado" valor={formatearFecha(viaje.horaSolicitud)} />
              <Dato titulo="Inicio" valor={formatearFecha(viaje.horaInicio)} />
              <Dato titulo="Fin" valor={formatearFecha(viaje.horaFinalizacion)} />
              <Dato titulo="Distancia" valor={formatearKm(viaje.distanciaKm)} />
              <Dato titulo="Duración" valor={formatearDuracion(viaje.duracion)} />
              <Dato titulo="Tarifa" valor={cobros.tarifa != null ? formatearPesos(cobros.tarifa) : '—'} />
              <Dato titulo="Cargo parcial" valor={cobros.cargo != null ? formatearPesos(cobros.cargo) : '—'} />
            </div>
          </section>

          <section className="tarjeta">
            <h3>Ofertas del emparejamiento</h3>
            {ofertas.length === 0 ? (
              <p className="suave">Sin ofertas registradas para este viaje.</p>
            ) : (
              <div className="tabla-envoltura">
                <table>
                  <thead>
                    <tr><th>Oferta</th><th>Conductor</th><th>Estado</th></tr>
                  </thead>
                  <tbody>
                    {ofertas.map((o) => (
                      <tr key={o.id}>
                        <td className="mono">{o.id}</td>
                        <td className="mono">{o.conductorId}</td>
                        <td>{o.estado}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </section>

          <Acciones viaje={viaje} alCambiar={() => cargar(viaje.id)} />
        </>
      )}
    </div>
  )
}

function Dato({ titulo, valor, mono }) {
  return (
    <div className="dato">
      <span>{titulo}</span>
      <strong className={mono ? 'mono' : ''} style={{ fontSize: '0.95rem', overflowWrap: 'anywhere' }}>{valor}</strong>
    </div>
  )
}

// Acciones de soporte para destrabar un viaje a mano (las mismas de los flujos alternativos de Bruno)
function Acciones({ viaje, alCambiar }) {
  const [conductorId, setConductorId] = useState('')
  const [motivo, setMotivo] = useState('')
  const [error, setError] = useState('')
  const [hecho, setHecho] = useState('')

  const ejecutar = async (accion, mensaje) => {
    setError('')
    setHecho('')
    try {
      await accion()
      setHecho(mensaje)
      await alCambiar()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <section className="tarjeta">
      <h3>Acciones de soporte</h3>
      <div className="rejilla-2">
        {viaje.estado === 'SOLICITADO' && (
          <form
            className="pila"
            style={{ gap: 8 }}
            onSubmit={(e) => {
              e.preventDefault()
              ejecutar(() => api.asignarConductor(viaje.id, conductorId.trim()), 'Conductor asignado.')
            }}
          >
            <label>
              Asignar conductor directamente
              <input placeholder="Id del conductor" value={conductorId} onChange={(e) => setConductorId(e.target.value)} />
            </label>
            <button className="boton secundario" disabled={!conductorId.trim()}>Asignar</button>
          </form>
        )}
        {viaje.estado === 'SOLICITADO' && (
          <form
            className="pila"
            style={{ gap: 8 }}
            onSubmit={(e) => {
              e.preventDefault()
              ejecutar(() => api.rechazarSinConductores(viaje.id, motivo), 'Solicitud rechazada.')
            }}
          >
            <label>
              Rechazar por falta de conductores
              <input placeholder="Motivo" value={motivo} onChange={(e) => setMotivo(e.target.value)} />
            </label>
            <button className="boton peligro" disabled={!motivo.trim()}>Rechazar</button>
          </form>
        )}
        {viaje.estado === 'FINALIZADO' && (
          <div className="pila" style={{ gap: 8 }}>
            <p className="suave">Recalcula la tarifa con la distancia y la duración registradas.</p>
            <button className="boton secundario" onClick={() => ejecutar(() => api.calcularTarifa(viaje.id), 'Tarifa calculada.')}>
              Calcular tarifa
            </button>
          </div>
        )}
      </div>
      {viaje.estado !== 'SOLICITADO' && viaje.estado !== 'FINALIZADO' && (
        <p className="suave">No hay acciones de soporte para un viaje en estado {ESTADOS_VIAJE[viaje.estado]?.texto ?? viaje.estado}.</p>
      )}
      <Aviso>{error}</Aviso>
      {hecho && <Aviso tipo="ok">{hecho}</Aviso>}
    </section>
  )
}
