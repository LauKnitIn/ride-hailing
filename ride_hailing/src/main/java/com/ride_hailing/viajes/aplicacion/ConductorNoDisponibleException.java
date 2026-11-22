package com.ride_hailing.viajes.aplicacion;

public class ConductorNoDisponibleException extends RuntimeException {
    public ConductorNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
