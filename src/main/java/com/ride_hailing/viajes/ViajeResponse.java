package com.ride_hailing.viajes;

import java.time.LocalDateTime;
import java.util.UUID;

public record ViajeResponse(
    UUID id,
    UUID pasajeroId,
    UUID conductorId,
    String origen,
    String destino,
    EstadoViaje.Valor estado,
    LocalDateTime horaSolicitud
) {
    public static ViajeResponse desde(Viaje viaje) {
        return new ViajeResponse(
            viaje.getId(),
            viaje.getPasajeroId(),
            viaje.getConductorId(),
            viaje.getOrigen(),
            viaje.getDestino(),
            viaje.getEstado().valor(),
            viaje.getHoraSolicitud()
        );
    }
}
