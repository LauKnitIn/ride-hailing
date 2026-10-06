package com.ride_hailing.viajes;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record ViajeResponse(
    UUID id,
    UUID pasajeroId,
    UUID conductorId,
    double latitudOrigen,
    double longitudOrigen,
    double latitudDestino,
    double longitudDestino,
    EstadoViaje.Valor estado,
    Instant horaSolicitud,
    Instant horaInicio,
    Instant horaFinalizacion,
    Double distanciaKm,
    Duration duracion
) {
    public static ViajeResponse desde(Viaje viaje) {
        return new ViajeResponse(
            viaje.getId(),
            viaje.getPasajeroId(),
            viaje.getConductorId(),
            viaje.getOrigen().latitud(),
            viaje.getOrigen().longitud(),
            viaje.getDestino().latitud(),
            viaje.getDestino().longitud(),
            viaje.getEstado().valor(),
            viaje.getHoraSolicitud(),
            viaje.getHoraInicio(),
            viaje.getHoraFinalizacion(),
            viaje.getDistanciaKm(),
            viaje.getDuracion()
        );
    }
}
