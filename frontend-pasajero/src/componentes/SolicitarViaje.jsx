import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import Mapa from '../comun/Mapa.jsx'
import { distanciaKm, formatearKm } from '../comun/utilidades.js'

const COLOR_ORIGEN = '#16a34a'
const COLOR_DESTINO = '#dc2626'

// HU-01: el pasajero indica origen y destino. Se eligen tocando el mapa o escribiendo las coordenadas.
export default function SolicitarViaje({ pasajeroId, alSolicitar }) {
  // Las coordenadas se guardan como texto para poder escribir "-" o borrar sin que el campo salte
  const [origen, setOrigen] = useState({ lat: '5.5353', lon: '-73.3678' })
  const [destino, setDestino] = useState({ lat: '5.5450', lon: '-73.3600' })
  const [eligiendo, setEligiendo] = useState('destino')
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)

  const numero = (p) => ({ lat: Number.parseFloat(p.lat), lon: Number.parseFloat(p.lon) })
  const valido = (p) => Number.isFinite(p.lat) && Number.isFinite(p.lon)
  const o = numero(origen)
  const d = numero(destino)

  const elegirEnMapa = (lat, lon) => {
    const punto = { lat: lat.toFixed(5), lon: lon.toFixed(5) }
    if (eligiendo === 'origen') {
      setOrigen(punto)
      setEligiendo('destino')
    } else {
      setDestino(punto)
    }
  }

  const solicitarViaje = async () => {
    setEnviando(true)
    setError('')
    try {
      const viaje = await api.solicitarViaje({
        pasajeroId,
        latitudOrigen: o.lat,
        longitudOrigen: o.lon,
        latitudDestino: d.lat,
        longitudDestino: d.lon,
      })
      alSolicitar(viaje.id)
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  const puntos = [
    valido(o) && { id: 'origen', ...o, color: COLOR_ORIGEN, etiqueta: 'Origen' },
    valido(d) && { id: 'destino', ...d, color: COLOR_DESTINO, etiqueta: 'Destino' },
  ].filter(Boolean)

  return (
    <section className="tarjeta">
      <div>
        <h2>¿A dónde vas?</h2>
        <p className="suave">Toca el mapa para fijar el punto seleccionado.</p>
      </div>

      <div className="pestanas" role="tablist" style={{ marginBottom: 0 }}>
        <button role="tab" aria-selected={eligiendo === 'origen'} onClick={() => setEligiendo('origen')}>
          Fijar origen
        </button>
        <button role="tab" aria-selected={eligiendo === 'destino'} onClick={() => setEligiendo('destino')}>
          Fijar destino
        </button>
      </div>

      <Mapa puntos={puntos} alHacerClic={elegirEnMapa} />
      <div className="leyenda">
        <span><i style={{ background: COLOR_ORIGEN }} />Origen</span>
        <span><i style={{ background: COLOR_DESTINO }} />Destino</span>
      </div>

      <div className="rejilla-2">
        <fieldset className="pila" style={{ border: 0, padding: 0, margin: 0, gap: 8 }}>
          <legend className="suave" style={{ marginBottom: 8 }}>Origen</legend>
          <div className="fila">
            <input aria-label="Latitud de origen" value={origen.lat} onChange={(e) => setOrigen({ ...origen, lat: e.target.value })} />
            <input aria-label="Longitud de origen" value={origen.lon} onChange={(e) => setOrigen({ ...origen, lon: e.target.value })} />
          </div>
        </fieldset>
        <fieldset className="pila" style={{ border: 0, padding: 0, margin: 0, gap: 8 }}>
          <legend className="suave" style={{ marginBottom: 8 }}>Destino</legend>
          <div className="fila">
            <input aria-label="Latitud de destino" value={destino.lat} onChange={(e) => setDestino({ ...destino, lat: e.target.value })} />
            <input aria-label="Longitud de destino" value={destino.lon} onChange={(e) => setDestino({ ...destino, lon: e.target.value })} />
          </div>
        </fieldset>
      </div>

      {valido(o) && valido(d) && (
        <p className="suave">Distancia en línea recta: <strong>{formatearKm(distanciaKm(o, d))}</strong></p>
      )}

      <Aviso>{error}</Aviso>
      <button className="boton ancho" onClick={solicitarViaje} disabled={enviando || !valido(o) || !valido(d)}>
        {enviando ? 'Solicitando…' : 'Solicitar viaje'}
      </button>
    </section>
  )
}
