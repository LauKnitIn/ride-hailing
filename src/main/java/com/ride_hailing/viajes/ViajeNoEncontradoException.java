package com.ride_hailing.viajes;

import java.util.UUID;

public class ViajeNoEncontradoException extends RuntimeException {
    public ViajeNoEncontradoException(UUID viajeId) {
        super("No existe un viaje con id " + viajeId);
    }
}
