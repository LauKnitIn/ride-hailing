package com.ride_hailing.pasajeros.dominio;

public class CorreoYaRegistradoException extends RuntimeException {
    public CorreoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}
