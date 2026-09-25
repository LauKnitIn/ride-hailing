package com.ride_hailing.viajes;

public class ConductorNoDisponibleException extends RuntimeException {
    public ConductorNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
