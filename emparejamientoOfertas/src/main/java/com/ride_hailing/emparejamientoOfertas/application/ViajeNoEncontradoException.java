package com.ride_hailing.emparejamientoOfertas.application;

public class ViajeNoEncontradoException extends RuntimeException {
    public ViajeNoEncontradoException(String message) {
        super(message);
    }
}
