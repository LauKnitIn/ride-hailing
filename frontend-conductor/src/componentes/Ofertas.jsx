import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { idCorto, useSondeo } from '../comun/utilidades.js'

// HU-04: ofertas que emparejamiento le propone a este conductor
export default function Ofertas({ conductor, disponible, alAceptar }) {
  const [ofertas, setOfertas] = useState([])
  const [modoManual, setModoManual] = useState(false)
  const [viajeBuscado, setViajeBuscado] = useState('')
  const [error, setError] = useState('')
  const [ocupado, setOcupado] = useState(false)

  const propuestasPara = (lista) => lista.filter((o) => o.conductorId === conductor.id && o.estado === 'PROPUESTA')

  // Si emparejamiento todavía no expone /api/ofertas/conductor/{id} (responde 404),
  // se pasa a buscar la oferta con el id del viaje que te comparta el pasajero
  useSondeo(
    async () => {
      try {
        setOfertas(propuestasPara(await api.ofertasDelConductor(conductor.id)))
        setError('')
      } catch (e) {
        if (e.status === 404) setModoManual(true)
        else setError(e.message)
      }
    },
    3000,
    disponible && !modoManual,
  )

  const buscarPorViaje = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const encontradas = propuestasPara(await api.ofertasDelViaje(viajeBuscado.trim()))
      setOfertas(encontradas)
      if (encontradas.length === 0) setError('Ese viaje no tiene ofertas propuestas para ti.')
    } catch (err) {
      setError(err.message)
    }
  }

  const responder = async (oferta, aceptar) => {
    setOcupado(true)
    setError('')
    try {
      if (aceptar) {
        const aceptada = await api.aceptarOferta(oferta.id)
        alAceptar(aceptada.viajeId)
      } else {
        await api.rechazarOferta(oferta.id)
        setOfertas((lista) => lista.filter((o) => o.id !== oferta.id))
      }
    } catch (err) {
      setError(err.message)
    } finally {
      setOcupado(false)
    }
  }

  return (
    <section className="tarjeta">
      <h2>Ofertas de viaje</h2>
      {!disponible && <Aviso tipo="info">Conéctate para recibir ofertas.</Aviso>}
      {disponible && !modoManual && ofertas.length === 0 && (
        <p className="suave">Esperando ofertas… emparejamiento te propone el viaje cuando eres el conductor más cercano.</p>
      )}
      {disponible && modoManual && (
        <>
          <Aviso tipo="info">
            emparejamiento-service todavía no lista las ofertas por conductor. Busca la oferta con el id del viaje.
          </Aviso>
          <form className="fila" onSubmit={buscarPorViaje}>
            <input
              aria-label="Id del viaje"
              placeholder="Id del viaje"
              value={viajeBuscado}
              onChange={(e) => setViajeBuscado(e.target.value)}
              style={{ flex: 1 }}
            />
            <button className="boton secundario" disabled={!viajeBuscado.trim()}>
              Buscar
            </button>
          </form>
        </>
      )}
      {ofertas.length > 0 && (
        <ul className="lista">
          {ofertas.map((o) => (
            <li key={o.id} className="item" style={{ cursor: 'default' }}>
              <span>
                <strong>Viaje {idCorto(o.viajeId)}</strong>
                <br />
                <span className="suave">Oferta {idCorto(o.id)}</span>
              </span>
              <span className="fila">
                <button className="boton chico" disabled={ocupado} onClick={() => responder(o, true)}>
                  Aceptar
                </button>
                <button className="boton secundario chico" disabled={ocupado} onClick={() => responder(o, false)}>
                  Rechazar
                </button>
              </span>
            </li>
          ))}
        </ul>
      )}
      <Aviso>{error}</Aviso>
    </section>
  )
}
