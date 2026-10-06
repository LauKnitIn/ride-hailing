package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.UUID;

public record ViajeAsignadoEvent(UUID viajeId, UUID conductorId, Instant momento) {
}
