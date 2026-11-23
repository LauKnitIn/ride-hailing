package com.ride_hailing.viajes.aplicacion;

public class ServicioNoDisponibleException extends RuntimeException {
    public ServicioNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
