package com.ride_hailing.calificacion.application;

import java.util.UUID;

public record DatosViajeCalificacion(
        UUID pasajeroId,
        UUID conductorId,
        boolean finalizado
) {}
