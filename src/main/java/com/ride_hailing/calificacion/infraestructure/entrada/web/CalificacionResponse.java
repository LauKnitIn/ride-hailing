package com.ride_hailing.calificacion.infraestructure.entrada.web;

import java.time.Instant;

import com.ride_hailing.calificacion.domain.Calificacion;

public record CalificacionResponse(
        Long id,
        Long viajeId,
        Long pasajeroId,
        Long conductorId,
        int puntuacion,
        String comentario,
        Instant fechaCreacion
) {
    public static CalificacionResponse desde(Calificacion calificacion) {
        return new CalificacionResponse(
                calificacion.getId(),
                calificacion.getViajeId(),
                calificacion.getPasajeroId(),
                calificacion.getConductorId(),
                calificacion.getPuntuacion().valor(),
                calificacion.getComentario(),
                calificacion.getFechaCreacion()
        );
    }
}
