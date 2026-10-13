package com.ride_hailing.calificacion.domain;

import java.util.UUID;

public class ClasificacionFactory {

    public static Calificacion crearCalificacion(UUID viajeId, UUID pasajeroId, UUID conductorId, Puntuacion puntuacion, String comentario) {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(UUID.randomUUID());
        calificacion.setViajeId(viajeId);
        calificacion.setPasajeroId(pasajeroId);
        calificacion.setConductorId(conductorId);
        calificacion.setPuntuacion(puntuacion);
        calificacion.setComentario(comentario);
        calificacion.setFechaCreacion(java.time.Instant.now());
        return calificacion;
    }

}
