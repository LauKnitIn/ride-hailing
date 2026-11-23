package com.ride_hailing.calificacion.domain;

import java.util.UUID;

/** Solo el pasajero del viaje puede calificar al conductor (HU-08). */
public class PasajeroNoAutorizadoException extends RuntimeException {
    public PasajeroNoAutorizadoException(UUID pasajeroId, UUID viajeId) {
        super("El pasajero " + pasajeroId + " no participo en el viaje " + viajeId);
    }
}
