package com.ride_hailing.calificacion.domain;

/** Solo se puede calificar un viaje finalizado y con conductor asignado (HU-08). */
public class ViajeNoCalificableException extends RuntimeException {
    public ViajeNoCalificableException(String message) {
        super(message);
    }
}
