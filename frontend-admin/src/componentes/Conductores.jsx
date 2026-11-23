import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'

const DISPONIBILIDAD = {
  INACTIVO: { texto: 'Desconectado', clase: 'neutra' },
  DISPONIBLE: { texto: 'Disponible', clase: 'bien' },
  EN_VIAJE: { texto: 'En viaje', clase: 'aviso-e' },
}

// Conductores disponibles, búsqueda por id y desconexión manual
export default function Conductores() {
  const [disponibles, setDisponibles] = useState(null)
  const [error, setError] = useState('')
  const [idBuscado, setIdBuscado] = useState('')
  const [encontrado, setEncontrado] = useState(null)
  const [errorBusqueda, setErrorBusqueda] = useState('')

  const cargar = () => {
    api
      .conductoresDisponibles()
      .then((lista) => {
        setError('')
        setDisponibles(lista)
      })
      .catch((e) => setError(e.message))
  }
  useEffect(cargar, [])

  const buscar = async (e) => {
    e.preventDefault()
    setErrorBusqueda('')
    setEncontrado(null)
    try {
      setEncontrado(await api.consultarConductor(idBuscado.trim()))
    } catch (err) {
      setErrorBusqueda(err.message)
    }
  }

  const desconectar = async (id) => {
    setError('')
    try {
      const actualizado = await api.desconectarConductor(id)
      if (encontrado?.id === id) setEncontrado(actualizado)
      cargar()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="pila">
      <section className="tarjeta">
        <div className="fila separada">
          <h2>Conductores disponibles {disponibles && <span className="suave">({disponibles.length})</span>}</h2>
          <button className="boton secundario chico" onClick={cargar}>Actualizar</button>
        </div>
        <Aviso>{error}</Aviso>
        {disponibles === null && !error && <p className="suave">Cargando…</p>}
        {disponibles?.length === 0 && <p className="suave">No hay conductores conectados.</p>}
        {disponibles?.length > 0 && (
          <div className="tabla-envoltura">
            <table>
              <thead>
                <tr><th>Nombre</th><th>Documento</th><th>Estado</th><th>Ubicación</th><th /></tr>
              </thead>
              <tbody>
                {disponibles.map((c) => (
                  <FilaConductor key={c.id} conductor={c} alDesconectar={desconectar} />
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      <section className="tarjeta">
        <h2>Buscar conductor</h2>
        <form className="fila" onSubmit={buscar}>
          <input aria-label="Id del conductor" placeholder="Id del conductor" value={idBuscado} onChange={(e) => setIdBuscado(e.target.value)} style={{ flex: 1 }} />
          <button className="boton" disabled={!idBuscado.trim()}>Buscar</button>
        </form>
        <Aviso>{errorBusqueda}</Aviso>
        {encontrado && (
          <div className="tabla-envoltura">
            <table>
              <tbody>
                <FilaConductor conductor={encontrado} alDesconectar={desconectar} />
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  )
}

function FilaConductor({ conductor: c, alDesconectar }) {
  const estado = DISPONIBILIDAD[c.disponibilidad] ?? { texto: c.disponibilidad, clase: 'neutra' }
  return (
    <tr>
      <td>
        <strong>{c.nombreCompleto}</strong>
        <br />
        <span className="suave mono">{c.id}</span>
      </td>
      <td>{c.tipoDocumento} {c.numeroDocumento}</td>
      <td><span className={`etiqueta ${estado.clase}`}>{estado.texto}</span></td>
      <td className="mono">{c.latitud != null ? `${c.latitud.toFixed(4)}, ${c.longitud.toFixed(4)}` : '—'}</td>
      <td>
        {c.disponibilidad === 'DISPONIBLE' && (
          <button className="boton peligro chico" onClick={() => alDesconectar(c.id)}>Desconectar</button>
        )}
      </td>
    </tr>
  )
}
