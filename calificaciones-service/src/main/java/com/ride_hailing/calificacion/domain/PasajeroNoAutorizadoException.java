package com.ride_hailing.calificacion.domain;

import java.util.UUID;

public class PasajeroNoAutorizadoException extends RuntimeException {

    public PasajeroNoAutorizadoException(UUID pasajeroId, UUID viajeId) {
        super("El pasajero " + pasajeroId + " no participo en el viaje " + viajeId);
    }
}
