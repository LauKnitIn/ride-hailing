package com.ride_hailing.viajes.dominio;

public class EstadoViajeInvalidoException extends RuntimeException {
    public EstadoViajeInvalidoException(String message) {
        super(message);
    }
    
}
