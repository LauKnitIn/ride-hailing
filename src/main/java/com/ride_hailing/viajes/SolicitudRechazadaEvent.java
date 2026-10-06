package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.UUID;

public record SolicitudRechazadaEvent(UUID viajeId, String motivo, Instant momento) {
}
