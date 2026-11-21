import { useState } from 'react'
import { guardar, leer } from './comun/utilidades.js'
import Ingreso from './componentes/Ingreso.jsx'
import Panel from './componentes/Panel.jsx'

export default function App() {
  // No hay autenticación en el backend: el navegador recuerda qué conductor está usando la app
  const [conductor, setConductor] = useState(() => leer('rh.conductor'))

  const ingresar = (datos) => {
    guardar('rh.conductor', datos)
    const recientes = (leer('rh.conductoresRecientes') ?? []).filter((c) => c.id !== datos.id)
    guardar('rh.conductoresRecientes', [{ id: datos.id, nombreCompleto: datos.nombreCompleto }, ...recientes].slice(0, 5))
    setConductor(datos)
  }
  const actualizar = (datos) => {
    guardar('rh.conductor', datos)
    setConductor(datos)
  }
  const salir = () => {
    guardar('rh.conductor', null)
    guardar('rh.viajeConductor', null)
    setConductor(null)
  }

  return (
    <div className={`app ${conductor ? '' : 'angosta'}`}>
      <header className="barra">
        <div className="marca">
          <span className="marca-icono" aria-hidden="true">▲</span>
          <div>
            Ride G3<small>Conductor</small>
          </div>
        </div>
        {conductor && (
          <div className="fila">
            <span className="suave">{conductor.nombreCompleto}</span>
            <button className="boton secundario chico" onClick={salir}>
              Salir
            </button>
          </div>
        )}
      </header>
      {conductor ? <Panel conductor={conductor} alActualizar={actualizar} /> : <Ingreso alIngresar={ingresar} />}
    </div>
  )
}
