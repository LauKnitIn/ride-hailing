package com.ride_hailing.calificacion.domain;

import java.util.UUID;

public class ViajeNoEncontradoException extends RuntimeException {

    public ViajeNoEncontradoException(UUID viajeId) {
        super("No se encontro el viaje " + viajeId);
    }
}
