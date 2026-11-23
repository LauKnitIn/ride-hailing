package com.ride_hailing.calificacion.application;

public class ServicioViajesNoDisponibleException extends RuntimeException {

    public ServicioViajesNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
