import { useEffect, useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'

// Registro de un pasajero nuevo, o elegir uno ya registrado (no hay login en el backend)
export default function Bienvenida({ alIngresar }) {
  const [formulario, setFormulario] = useState({ nombre: '', correo: '', telefono: '' })
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [existentes, setExistentes] = useState(null)
  const [errorLista, setErrorLista] = useState('')

  useEffect(() => {
    api.listarPasajeros().then(setExistentes).catch((e) => setErrorLista(e.message))
  }, [])

  const cambiar = (campo) => (e) => setFormulario({ ...formulario, [campo]: e.target.value })

  const registrar = async (e) => {
    e.preventDefault()
    setEnviando(true)
    setError('')
    try {
      alIngresar(await api.registrarPasajero(formulario))
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="pila">
      <form className="tarjeta" onSubmit={registrar}>
        <div>
          <h2>Crea tu cuenta</h2>
          <p className="suave">Con tu cuenta puedes pedir viajes y seguirlos en vivo.</p>
        </div>
        <label>
          Nombre
          <input value={formulario.nombre} onChange={cambiar('nombre')} required autoComplete="name" />
        </label>
        <label>
          Correo
          <input type="email" value={formulario.correo} onChange={cambiar('correo')} required autoComplete="email" />
        </label>
        <label>
          Teléfono (solo dígitos, 7 a 15)
          <input value={formulario.telefono} onChange={cambiar('telefono')} required inputMode="numeric" pattern="\d{7,15}" />
        </label>
        <Aviso>{error}</Aviso>
        <button className="boton ancho" disabled={enviando}>
          {enviando ? 'Registrando…' : 'Registrarme'}
        </button>
      </form>

      <section className="tarjeta">
        <h2>¿Ya tienes cuenta?</h2>
        <Aviso>{errorLista}</Aviso>
        {existentes === null && !errorLista && <p className="suave">Cargando pasajeros…</p>}
        {existentes?.length === 0 && <p className="suave">Todavía no hay pasajeros registrados.</p>}
        {existentes?.length > 0 && (
          <ul className="lista">
            {existentes.map((p) => (
              <li key={p.id}>
                <button onClick={() => alIngresar(p)}>
                  <span>
                    <strong>{p.nombre}</strong>
                    <br />
                    <span className="suave">{p.correo}</span>
                  </span>
                  <span aria-hidden="true">→</span>
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  )
}
