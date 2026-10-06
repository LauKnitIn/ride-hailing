package com.ride_hailing.viajes.infraestructura.entrada.web;

import java.util.UUID;

public record SolicitarViajeRequest(
    UUID pasajeroId,
    double latitudOrigen,
    double longitudOrigen,
    double latitudDestino,
    double longitudDestino
) {
}
