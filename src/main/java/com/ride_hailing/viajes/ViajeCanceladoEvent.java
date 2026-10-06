package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.UUID;

public record ViajeCanceladoEvent(
    UUID viajeId,
    UUID conductorId,
    boolean requiereCargoParcial,
    Instant momento
) {
}