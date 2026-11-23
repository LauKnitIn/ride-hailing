import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { ESTADOS_VIAJE, formatearFecha, formatearKm, idCorto } from '../comun/utilidades.js'

// Pasajeros registrados y el historial de viajes de cada uno
export default function Pasajeros({ alAbrirViaje }) {
  const [pasajeros, setPasajeros] = useState(null)
  const [error, setError] = useState('')
  const [elegido, setElegido] = useState(null)
  const [viajes, setViajes] = useState(null)
  const [errorViajes, setErrorViajes] = useState('')

  useEffect(() => {
    api.listarPasajeros().then(setPasajeros).catch((e) => setError(e.message))
  }, [])

  const verHistorial = async (pasajero) => {
    setElegido(pasajero)
    setViajes(null)
    setErrorViajes('')
    try {
      const lista = await api.historial(pasajero.id)
      setViajes([...lista].sort((a, b) => String(b.horaSolicitud).localeCompare(String(a.horaSolicitud))))
    } catch (e) {
      setErrorViajes(e.message)
    }
  }

  return (
    <div className="rejilla-2" style={{ alignItems: 'start' }}>
      <section className="tarjeta">
        <h2>Pasajeros {pasajeros && <span className="suave">({pasajeros.length})</span>}</h2>
        <Aviso>{error}</Aviso>
        {pasajeros === null && !error && <p className="suave">Cargando…</p>}
        {pasajeros?.length === 0 && <p className="suave">No hay pasajeros registrados.</p>}
        {pasajeros?.length > 0 && (
          <div className="tabla-envoltura">
            <table>
              <thead>
                <tr><th>Nombre</th><th>Correo</th><th>Teléfono</th></tr>
              </thead>
              <tbody>
                {pasajeros.map((p) => (
                  <tr key={p.id} className="clic" onClick={() => verHistorial(p)} aria-selected={elegido?.id === p.id}>
                    <td><strong>{p.nombre}</strong></td>
                    <td>{p.correo}</td>
                    <td>{p.telefono}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      <section className="tarjeta">
        <h2>{elegido ? `Viajes de ${elegido.nombre}` : 'Historial'}</h2>
        {!elegido && <p className="suave">Elige un pasajero para ver sus viajes.</p>}
        {elegido && <p className="suave mono">id: {elegido.id}</p>}
        <Aviso>{errorViajes}</Aviso>
        {elegido && viajes === null && !errorViajes && <p className="suave">Cargando…</p>}
        {viajes?.length === 0 && <p className="suave">Este pasajero no tiene viajes.</p>}
        {viajes?.length > 0 && (
          <div className="tabla-envoltura">
            <table>
              <thead>
                <tr><th>Viaje</th><th>Solicitado</th><th>Estado</th><th>Distancia</th></tr>
              </thead>
              <tbody>
                {viajes.map((v) => (
                  <tr key={v.id} className="clic" onClick={() => alAbrirViaje(v.id)}>
                    <td className="mono">{idCorto(v.id)}</td>
                    <td>{formatearFecha(v.horaSolicitud)}</td>
                    <td>
                      <span className={`etiqueta ${ESTADOS_VIAJE[v.estado]?.clase ?? 'neutra'}`}>
                        {ESTADOS_VIAJE[v.estado]?.texto ?? v.estado}
                      </span>
                    </td>
                    <td>{formatearKm(v.distanciaKm)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  )
}
