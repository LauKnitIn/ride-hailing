import { useState } from 'react'
import Conductores from './componentes/Conductores.jsx'
import Pasajeros from './componentes/Pasajeros.jsx'
import Servicios from './componentes/Servicios.jsx'
import Viajes from './componentes/Viajes.jsx'

const PESTANAS = [
  { id: 'servicios', texto: 'Servicios' },
  { id: 'pasajeros', texto: 'Pasajeros' },
  { id: 'conductores', texto: 'Conductores' },
  { id: 'viajes', texto: 'Viajes' },
]

export default function App() {
  const [pestana, setPestana] = useState('servicios')
  const [viajeId, setViajeId] = useState('')

  const abrirViaje = (id) => {
    setViajeId(id)
    setPestana('viajes')
  }

  return (
    <div className="app">
      <header className="barra">
        <div className="marca">
          <span className="marca-icono" aria-hidden="true">■</span>
          <div>
            Ride G3<small>Administrador</small>
          </div>
        </div>
        <span className="suave">Todo pasa por el API Gateway</span>
      </header>
      <nav className="pestanas" role="tablist">
        {PESTANAS.map((p) => (
          <button key={p.id} role="tab" aria-selected={pestana === p.id} onClick={() => setPestana(p.id)}>
            {p.texto}
          </button>
        ))}
      </nav>
      {pestana === 'servicios' && <Servicios />}
      {pestana === 'pasajeros' && <Pasajeros alAbrirViaje={abrirViaje} />}
      {pestana === 'conductores' && <Conductores />}
      {pestana === 'viajes' && <Viajes key={viajeId} viajeInicial={viajeId} />}
    </div>
  )
}
