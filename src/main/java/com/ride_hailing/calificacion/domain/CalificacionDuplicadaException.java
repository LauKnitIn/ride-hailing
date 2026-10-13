package com.ride_hailing.calificacion.domain;

import java.util.UUID;

/** Regla de negocio: un viaje no puede calificarse dos veces. */
public class CalificacionDuplicadaException extends RuntimeException {
    public CalificacionDuplicadaException(UUID viajeId) {
        super("El viaje " + viajeId + " ya fue calificado");
    }
}
