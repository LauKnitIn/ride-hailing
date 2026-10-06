package com.ride_hailing.pasajeros.dominio;

import java.util.UUID;

public class PasajeroNoEncontradoException extends RuntimeException {
    public PasajeroNoEncontradoException(UUID pasajeroId) {
        super("No existe un pasajero con id " + pasajeroId);
    }
}
