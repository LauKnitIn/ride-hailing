package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.UUID;

public record ViajeSolicitadoEvent(
    UUID viajeId,
    UUID pasajeroId,
    double latitudOrigen,
    double longitudOrigen,
    Instant momento
) {
}
