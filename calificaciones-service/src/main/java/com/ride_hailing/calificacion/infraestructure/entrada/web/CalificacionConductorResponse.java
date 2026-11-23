package com.ride_hailing.calificacion.infraestructure.entrada.web;

import java.time.Instant;

import com.ride_hailing.calificacion.domain.Calificacion;

public record CalificacionConductorResponse(
        int puntuacion,
        String comentario,
        Instant fechaCreacion
) {
    public static CalificacionConductorResponse desde(Calificacion calificacion) {
        return new CalificacionConductorResponse(
                calificacion.getPuntuacion().valor(),
                calificacion.getComentario(),
                calificacion.getFechaCreacion());
    }
}
