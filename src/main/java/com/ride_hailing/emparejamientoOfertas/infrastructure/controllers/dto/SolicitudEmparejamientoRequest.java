package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers.dto;

import java.util.UUID;

public record SolicitudEmparejamientoRequest(
        UUID viajeId,
        double origenLat,
        double origenLon
) {

}
