import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { formatearPesos, useSondeo } from '../comun/utilidades.js'

// HU-07 (tarifa) y cargo parcial por cancelación. tarifas-service los calcula al recibir el evento
// de Kafka, unos segundos después de que el viaje termina: mientras tanto responde 404 y se reintenta.
export default function Cobro({ viajeId, tipo }) {
  const [monto, setMonto] = useState(null)
  const [intentos, setIntentos] = useState(0)
  const [error, setError] = useState('')
  const maximo = tipo === 'tarifa' ? 20 : 5
  const buscando = monto == null && !error && intentos < maximo

  useSondeo(
    async () => {
      try {
        const respuesta = tipo === 'tarifa' ? await api.tarifa(viajeId) : await api.cargoParcial(viajeId)
        setMonto(respuesta.monto)
      } catch (e) {
        if (e.status === 404) setIntentos((n) => n + 1)
        else setError(e.message)
      }
    },
    3000,
    buscando,
  )

  const reintentar = () => {
    setError('')
    setIntentos(0)
  }

  const titulo = tipo === 'tarifa' ? 'Tarifa del viaje' : 'Cargo por cancelación'
  return (
    <section className="tarjeta">
      <div className="fila separada">
        <h3>{titulo}</h3>
        {monto != null && <strong style={{ fontSize: '1.4rem' }}>{formatearPesos(monto)}</strong>}
      </div>
      {buscando && <p className="suave">{tipo === 'tarifa' ? 'Calculando la tarifa…' : 'Revisando si hay cargo…'}</p>}
      {monto == null && !buscando && !error && (
        <p className="suave">
          {tipo === 'tarifa' ? 'La tarifa todavía no está disponible.' : 'Esta cancelación no generó cargo.'}
        </p>
      )}
      {tipo === 'tarifa' && monto != null && <p className="suave">Calculada con la distancia y el tiempo reales del viaje.</p>}
      <Aviso>{error}</Aviso>
      {(error || (monto == null && !buscando)) && (
        <button className="boton secundario" onClick={reintentar}>
          Consultar de nuevo
        </button>
      )}
    </section>
  )
}
