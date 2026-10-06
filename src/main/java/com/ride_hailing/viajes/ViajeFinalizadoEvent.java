package com.ride_hailing.viajes;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record ViajeFinalizadoEvent(
    UUID viajeId,
    UUID conductorId,
    double distanciaKm,
    Duration duracion,
    Instant momento
) {
}
