package com.ride_hailing.calificacion.infraestructure.entrada.web;

import java.util.UUID;

public record CalificarConductorRequest(
        UUID viajeId,
        UUID pasajeroId,
        int puntuacion,
        String comentario
) {
}
