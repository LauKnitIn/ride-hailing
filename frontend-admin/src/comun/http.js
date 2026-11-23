// Cliente HTTP mínimo. Todas las rutas empiezan por /api: en desarrollo, Vite las
// reenvía al API Gateway (vite.config.js), así el navegador nunca habla con un
// microservicio directamente y no hace falta configurar CORS.

export class ApiError extends Error {
  constructor(status, mensaje) {
    super(mensaje)
    this.status = status
  }
}

export async function solicitar(metodo, ruta, cuerpo, tipo = 'json') {
  const opciones = { method: metodo, headers: {} }
  if (cuerpo !== undefined) {
    if (tipo === 'texto') {
      // Los endpoints de cancelar y rechazar reciben el motivo como texto plano (@RequestBody String)
      opciones.headers['Content-Type'] = 'text/plain'
      opciones.body = cuerpo
    } else {
      opciones.headers['Content-Type'] = 'application/json'
      opciones.body = JSON.stringify(cuerpo)
    }
  }

  let respuesta
  try {
    respuesta = await fetch(ruta, opciones)
  } catch {
    throw new ApiError(0, 'No hay conexión con el servidor. ¿Está corriendo el Gateway?')
  }

  const texto = await respuesta.text()
  const datos = texto ? leerJson(texto) : null
  if (!respuesta.ok) {
    throw new ApiError(respuesta.status, mensajeDeError(respuesta.status, datos))
  }
  return datos
}

function leerJson(texto) {
  try {
    return JSON.parse(texto)
  } catch {
    return texto
  }
}

function mensajeDeError(status, datos) {
  // Los manejadores de errores de los servicios responden {"error": "..."}
  if (datos && typeof datos === 'object' && typeof datos.error === 'string' && status !== 503) {
    return datos.error
  }
  if (status === 502 || status === 503 || status === 504) {
    return (datos && typeof datos === 'object' && datos.error && datos.error !== 'Service Unavailable')
      ? datos.error
      : 'El servicio no está disponible en este momento. Intenta de nuevo en unos segundos.'
  }
  if (status === 404) return 'No se encontró lo que buscabas.'
  return `Error ${status} del servidor.`
}
