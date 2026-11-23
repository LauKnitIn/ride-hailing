package com.ride_hailing.calificacion.domain;

public class ViajeNoCalificableException extends RuntimeException {

    public ViajeNoCalificableException(String message) {
        super(message);
    }
}
