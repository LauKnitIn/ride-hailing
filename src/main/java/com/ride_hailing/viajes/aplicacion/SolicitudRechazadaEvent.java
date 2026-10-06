package com.ride_hailing.viajes.aplicacion;

import java.time.Instant;
import java.util.UUID;

public record SolicitudRechazadaEvent(UUID viajeId, String motivo, Instant momento) {
}
