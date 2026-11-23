import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'

// HU-08: calificar al conductor al finalizar el viaje (una sola vez por viaje)
export default function Calificar({ viajeId, pasajeroId }) {
  const [puntuacion, setPuntuacion] = useState(0)
  const [comentario, setComentario] = useState('')
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [enviada, setEnviada] = useState(false)

  const enviar = async (e) => {
    e.preventDefault()
    setEnviando(true)
    setError('')
    try {
      await api.calificar({ viajeId, pasajeroId, puntuacion, comentario })
      setEnviada(true)
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  if (enviada) {
    return (
      <section className="tarjeta">
        <Aviso tipo="ok">Gracias, tu calificación quedó registrada.</Aviso>
      </section>
    )
  }
  return (
    <form className="tarjeta" onSubmit={enviar}>
      <h3>¿Cómo estuvo tu conductor?</h3>
      <div className="estrellas" role="radiogroup" aria-label="Puntuación de 1 a 5">
        {[1, 2, 3, 4, 5].map((n) => (
          <button
            key={n}
            type="button"
            role="radio"
            aria-checked={puntuacion === n}
            aria-label={`${n} de 5`}
            className={n <= puntuacion ? 'llena' : ''}
            onClick={() => setPuntuacion(n)}
          >
            ★
          </button>
        ))}
      </div>
      <label>
        Comentario (opcional)
        <textarea value={comentario} onChange={(e) => setComentario(e.target.value)} />
      </label>
      <Aviso>{error}</Aviso>
      <button className="boton" disabled={enviando || puntuacion === 0}>
        {enviando ? 'Enviando…' : 'Enviar calificación'}
      </button>
    </form>
  )
}
