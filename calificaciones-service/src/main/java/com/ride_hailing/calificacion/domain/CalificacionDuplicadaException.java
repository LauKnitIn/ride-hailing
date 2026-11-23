package com.ride_hailing.calificacion.domain;

import java.util.UUID;

public class CalificacionDuplicadaException extends RuntimeException {

    public CalificacionDuplicadaException(UUID viajeId) {
        super("El viaje " + viajeId + " ya fue calificado");
    }
}
