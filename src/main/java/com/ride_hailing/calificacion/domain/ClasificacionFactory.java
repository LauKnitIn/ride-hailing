package com.ride_hailing.calificacion.domain;

public class ClasificacionFactory {

    public static Calificacion crearCalificacion(Long viajeId, Long pasajeroId, Long conductorId, Puntuacion puntuacion, String comentario) {
        Calificacion calificacion = new Calificacion();
        calificacion.setViajeId(viajeId);
        calificacion.setPasajeroId(pasajeroId);
        calificacion.setConductorId(conductorId);
        calificacion.setPuntuacion(puntuacion);
        calificacion.setComentario(comentario);
        calificacion.setFechaCreacion(java.time.Instant.now());
        return calificacion;
    }

}
