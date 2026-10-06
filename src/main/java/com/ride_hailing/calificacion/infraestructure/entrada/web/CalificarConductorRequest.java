package com.ride_hailing.calificacion.infraestructure.entrada.web;

public record CalificarConductorRequest(
        Long viajeId,
        int puntuacion,
        String comentario
) {
}
