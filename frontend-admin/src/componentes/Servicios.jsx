import { useState } from 'react'
import { SONDAS, sondear } from '../api.js'
import { useSondeo } from '../comun/utilidades.js'

// Estado de los seis microservicios vistos desde el Gateway, refrescado cada 10 s
export default function Servicios() {
  const [resultados, setResultados] = useState({})
  const [revisado, setRevisado] = useState(null)

  const revisar = async () => {
    const pares = await Promise.all(SONDAS.map(async (s) => [s.servicio, await sondear(s.ruta)]))
    setResultados(Object.fromEntries(pares))
    setRevisado(new Date())
  }
  useSondeo(revisar, 10000)

  const vivos = Object.values(resultados).filter((r) => r.vivo).length

  return (
    <div className="pila">
      <section className="tarjeta">
        <div className="fila separada">
          <div>
            <h2>
              {revisado ? `${vivos} de ${SONDAS.length} servicios responden` : 'Revisando servicios…'}
            </h2>
            <p className="suave">
              Cada tarjeta llama una ruta del Gateway. Cualquier respuesta del servicio, incluso 404, significa que está
              registrado en Eureka; 503 significa que el Gateway no lo encuentra.
            </p>
          </div>
          <button className="boton secundario" onClick={revisar}>
            Revisar ahora
          </button>
        </div>
        {revisado && <p className="suave">Última revisión: {revisado.toLocaleTimeString('es-CO')}</p>}
      </section>

      <div className="rejilla-2">
        {SONDAS.map((s) => {
          const r = resultados[s.servicio]
          return (
            <section key={s.servicio} className="tarjeta" style={{ gap: 8 }}>
              <div className="fila separada">
                <h3>{s.servicio}</h3>
                {r ? (
                  <span className={`etiqueta ${r.vivo ? 'bien' : 'mal'}`}>{r.vivo ? 'Responde' : 'Sin respuesta'}</span>
                ) : (
                  <span className="etiqueta neutra">…</span>
                )}
              </div>
              <p className="suave mono">GET {s.ruta}</p>
              {r && (
                <p className="suave">
                  {r.status ? `HTTP ${r.status}` : 'Sin conexión con el Gateway'}
                  {r.ms != null && ` · ${r.ms} ms`}
                </p>
              )}
            </section>
          )
        })}
      </div>
    </div>
  )
}
