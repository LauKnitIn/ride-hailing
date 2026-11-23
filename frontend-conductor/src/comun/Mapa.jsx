import { useEffect } from 'react'
import { CircleMarker, MapContainer, Polyline, TileLayer, Tooltip, useMap, useMapEvents } from 'react-leaflet'
import { CENTRO_TUNJA } from './utilidades.js'

// Mapa de OpenStreetMap con puntos de colores. Se usan CircleMarker (no Marker) porque los
// íconos por defecto de Leaflet se rompen al empaquetar con Vite.
//
// puntos: [{ id, lat, lon, color, etiqueta }]   linea: [{lat, lon}, {lat, lon}] opcional
// alHacerClic(lat, lon): opcional, para elegir un punto en el mapa
export default function Mapa({ puntos = [], linea, alHacerClic }) {
  const centro = puntos[0] ?? CENTRO_TUNJA
  return (
    <MapContainer className="mapa" center={[centro.lat, centro.lon]} zoom={14} scrollWheelZoom>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {puntos.map((p) => (
        <CircleMarker
          key={p.id}
          center={[p.lat, p.lon]}
          radius={9}
          pathOptions={{ color: '#ffffff', weight: 3, fillColor: p.color, fillOpacity: 1 }}
        >
          {p.etiqueta && <Tooltip direction="top" offset={[0, -8]}>{p.etiqueta}</Tooltip>}
        </CircleMarker>
      ))}
      {linea && (
        <Polyline
          positions={linea.map((p) => [p.lat, p.lon])}
          pathOptions={{ color: '#374151', weight: 3, dashArray: '6 8' }}
        />
      )}
      <Encuadre puntos={puntos} />
      {alHacerClic && <ClicEnMapa alHacerClic={alHacerClic} />}
    </MapContainer>
  )
}

function ClicEnMapa({ alHacerClic }) {
  useMapEvents({ click: (e) => alHacerClic(e.latlng.lat, e.latlng.lng) })
  return null
}

// Ajusta el zoom para que se vean todos los puntos cuando cambia cuántos hay
function Encuadre({ puntos }) {
  const mapa = useMap()
  const cantidad = puntos.length
  useEffect(() => {
    if (cantidad >= 2) {
      mapa.fitBounds(puntos.map((p) => [p.lat, p.lon]), { padding: [40, 40], maxZoom: 16 })
    }
    // Solo se reencuadra cuando aparece o desaparece un punto, no en cada movimiento
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [cantidad, mapa])
  return null
}
