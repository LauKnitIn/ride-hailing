// Mensaje de error, información o éxito. No muestra nada si no hay texto.
export default function Aviso({ tipo = 'error', children }) {
  if (!children) return null
  return (
    <div className={`aviso ${tipo}`} role={tipo === 'error' ? 'alert' : 'status'}>
      {children}
    </div>
  )
}
