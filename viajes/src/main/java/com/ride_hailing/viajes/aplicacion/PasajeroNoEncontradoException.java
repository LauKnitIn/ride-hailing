package com.ride_hailing.viajes.aplicacion;

import java.util.UUID;

public class PasajeroNoEncontradoException extends RuntimeException {
    public PasajeroNoEncontradoException(UUID pasajeroId) {
        super("No existe un pasajero con id " + pasajeroId);
    }
}
