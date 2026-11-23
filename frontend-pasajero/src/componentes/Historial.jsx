import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { ESTADOS_VIAJE, formatearFecha, formatearKm } from '../comun/utilidades.js'

// HU-09: historial de viajes del pasajero, incluidos los cancelados a mitad de camino
export default function Historial({ pasajeroId, alAbrir }) {
  const [viajes, setViajes] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    api
      .historial(pasajeroId)
      .then((lista) => setViajes([...lista].sort((a, b) => String(b.horaSolicitud).localeCompare(String(a.horaSolicitud)))))
      .catch((e) => setError(e.message))
  }, [pasajeroId])

  return (
    <section className="tarjeta">
      <h2>Mis viajes</h2>
      <Aviso>{error}</Aviso>
      {viajes === null && !error && <p className="suave">Cargando…</p>}
      {viajes?.length === 0 && <p className="suave">Todavía no has hecho ningún viaje.</p>}
      {viajes?.length > 0 && (
        <ul className="lista">
          {viajes.map((v) => (
            <li key={v.id}>
              <button onClick={() => alAbrir(v.id)}>
                <span>
                  <strong>{formatearFecha(v.horaSolicitud)}</strong>
                  <br />
                  <span className="suave">{v.distanciaKm != null ? formatearKm(v.distanciaKm) : 'Sin recorrido'}</span>
                </span>
                <span className={`etiqueta ${ESTADOS_VIAJE[v.estado]?.clase ?? 'neutra'}`}>
                  {ESTADOS_VIAJE[v.estado]?.texto ?? v.estado}
                </span>
              </button>
            </li>
          ))}
        </ul>
      )}
    </section>
  )
}
