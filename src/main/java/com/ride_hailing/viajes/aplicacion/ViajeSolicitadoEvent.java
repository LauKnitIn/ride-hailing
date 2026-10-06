package com.ride_hailing.viajes.aplicacion;

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
