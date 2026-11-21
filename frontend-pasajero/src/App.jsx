import { useState } from 'react'
import { guardar, leer } from './comun/utilidades.js'
import Bienvenida from './componentes/Bienvenida.jsx'
import Historial from './componentes/Historial.jsx'
import Perfil from './componentes/Perfil.jsx'
import SolicitarViaje from './componentes/SolicitarViaje.jsx'
import ViajeActivo from './componentes/ViajeActivo.jsx'

const PESTANAS = [
  { id: 'viaje', texto: 'Viaje' },
  { id: 'historial', texto: 'Mis viajes' },
  { id: 'perfil', texto: 'Perfil' },
]

export default function App() {
  // No hay autenticación en el backend: el navegador recuerda qué pasajero está usando la app
  const [pasajero, setPasajero] = useState(() => leer('rh.pasajero'))
  const [viajeId, setViajeId] = useState(() => leer('rh.viajeActivo'))
  const [pestana, setPestana] = useState('viaje')

  const ingresar = (datos) => {
    guardar('rh.pasajero', datos)
    setPasajero(datos)
  }
  const salir = () => {
    guardar('rh.pasajero', null)
    guardar('rh.viajeActivo', null)
    setPasajero(null)
    setViajeId(null)
  }
  const abrirViaje = (id) => {
    guardar('rh.viajeActivo', id)
    setViajeId(id)
    setPestana('viaje')
  }
  const cerrarViaje = () => {
    guardar('rh.viajeActivo', null)
    setViajeId(null)
  }

  return (
    <div className="app angosta">
      <header className="barra">
        <div className="marca">
          <span className="marca-icono" aria-hidden="true">◉</span>
          <div>
            Ride G3<small>Pasajero</small>
          </div>
        </div>
        {pasajero && (
          <button className="boton secundario chico" onClick={salir}>
            Salir
          </button>
        )}
      </header>

      {!pasajero ? (
        <Bienvenida alIngresar={ingresar} />
      ) : (
        <>
          <p className="suave" style={{ marginBottom: 12 }}>
            Hola, <strong>{pasajero.nombre}</strong>
          </p>
          <nav className="pestanas" role="tablist">
            {PESTANAS.map((p) => (
              <button key={p.id} role="tab" aria-selected={pestana === p.id} onClick={() => setPestana(p.id)}>
                {p.texto}
              </button>
            ))}
          </nav>

          {pestana === 'viaje' &&
            (viajeId ? (
              <ViajeActivo viajeId={viajeId} pasajeroId={pasajero.id} alCerrar={cerrarViaje} />
            ) : (
              <SolicitarViaje pasajeroId={pasajero.id} alSolicitar={abrirViaje} />
            ))}
          {pestana === 'historial' && <Historial pasajeroId={pasajero.id} alAbrir={abrirViaje} />}
          {pestana === 'perfil' && <Perfil pasajero={pasajero} alActualizar={ingresar} />}
        </>
      )}
    </div>
  )
}
