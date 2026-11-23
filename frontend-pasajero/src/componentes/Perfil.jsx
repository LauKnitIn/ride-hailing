import { useState } from 'react'
import { api } from '../api.js'
import Aviso from '../comun/Aviso.jsx'

// Actualizar nombre y teléfono (el correo no se cambia desde aquí)
export default function Perfil({ pasajero, alActualizar }) {
  const [nombre, setNombre] = useState(pasajero.nombre)
  const [telefono, setTelefono] = useState(pasajero.telefono ?? '')
  const [error, setError] = useState('')
  const [listo, setListo] = useState(false)
  const [enviando, setEnviando] = useState(false)

  const guardarCambios = async (e) => {
    e.preventDefault()
    setEnviando(true)
    setError('')
    setListo(false)
    try {
      await api.actualizarPerfil(pasajero.id, { nombre, correo: pasajero.correo, telefono })
      alActualizar(await api.consultarPasajero(pasajero.id))
      setListo(true)
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="tarjeta" onSubmit={guardarCambios}>
      <h2>Perfil</h2>
      <label>
        Nombre
        <input value={nombre} onChange={(e) => setNombre(e.target.value)} required />
      </label>
      <label>
        Correo
        <input value={pasajero.correo} disabled />
      </label>
      <label>
        Teléfono
        <input value={telefono} onChange={(e) => setTelefono(e.target.value)} required inputMode="numeric" pattern="\d{7,15}" />
      </label>
      <p className="suave mono">id: {pasajero.id}</p>
      <Aviso>{error}</Aviso>
      {listo && <Aviso tipo="ok">Perfil actualizado.</Aviso>}
      <button className="boton" disabled={enviando}>
        {enviando ? 'Guardando…' : 'Guardar cambios'}
      </button>
    </form>
  )
}
