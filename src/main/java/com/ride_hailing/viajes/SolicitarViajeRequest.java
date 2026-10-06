package com.ride_hailing.viajes;

import java.util.UUID;

public record SolicitarViajeRequest(UUID pasajeroId, String origen, String destino) {
}
