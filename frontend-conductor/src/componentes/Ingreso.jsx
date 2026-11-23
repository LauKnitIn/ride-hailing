import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'
import { leer } from '../comun/utilidades.js'

// Registro de un conductor nuevo, o entrar con el id de uno ya registrado
export default function Ingreso({ alIngresar }) {
  const [formulario, setFormulario] = useState({
    nombreCompleto: '',
    tipoDocumento: 'CC',
    numeroDocumento: '',
    fechaNacimiento: '',
  })
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)
  const [idExistente, setIdExistente] = useState('')
  const [errorId, setErrorId] = useState('')
  const recientes = leer('rh.conductoresRecientes') ?? []

  const cambiar = (campo) => (e) => setFormulario({ ...formulario, [campo]: e.target.value })

  const registrar = async (e) => {
    e.preventDefault()
    setEnviando(true)
    setError('')
    try {
      alIngresar(await api.registrarConductor(formulario))
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  const entrar = async (id) => {
    setErrorId('')
    try {
      alIngresar(await api.consultarConductor(id.trim()))
    } catch (err) {
      setErrorId(err.message)
    }
  }

  return (
    <div className="pila">
      <form className="tarjeta" onSubmit={registrar}>
        <div>
          <h2>Regístrate como conductor</h2>
          <p className="suave">Debes ser mayor de edad.</p>
        </div>
        <label>
          Nombre completo
          <input value={formulario.nombreCompleto} onChange={cambiar('nombreCompleto')} required />
        </label>
        <div className="rejilla-2">
          <label>
            Tipo de documento
            <select value={formulario.tipoDocumento} onChange={cambiar('tipoDocumento')}>
              <option value="CC">Cédula de ciudadanía</option>
              <option value="CE">Cédula de extranjería</option>
              <option value="PA">Pasaporte</option>
            </select>
          </label>
          <label>
            Número de documento
            <input value={formulario.numeroDocumento} onChange={cambiar('numeroDocumento')} required />
          </label>
        </div>
        <label>
          Fecha de nacimiento
          <input type="date" value={formulario.fechaNacimiento} onChange={cambiar('fechaNacimiento')} required />
        </label>
        <Aviso>{error}</Aviso>
        <button className="boton ancho" disabled={enviando}>
          {enviando ? 'Registrando…' : 'Registrarme'}
        </button>
      </form>

      <section className="tarjeta">
        <h2>Ya estoy registrado</h2>
        {recientes.length > 0 && (
          <ul className="lista">
            {recientes.map((c) => (
              <li key={c.id}>
                <button onClick={() => entrar(c.id)}>
                  <strong>{c.nombreCompleto}</strong>
                  <span className="suave mono">{c.id.slice(0, 8)}</span>
                </button>
              </li>
            ))}
          </ul>
        )}
        <form
          className="fila"
          onSubmit={(e) => {
            e.preventDefault()
            entrar(idExistente)
          }}
        >
          <input
            aria-label="Id del conductor"
            placeholder="Pega tu id de conductor"
            value={idExistente}
            onChange={(e) => setIdExistente(e.target.value)}
            style={{ flex: 1 }}
          />
          <button className="boton secundario" disabled={!idExistente.trim()}>
            Entrar
          </button>
        </form>
        <Aviso>{errorId}</Aviso>
      </section>
    </div>
  )
}
