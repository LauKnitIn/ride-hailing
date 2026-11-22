package com.ride_hailing.emparejamientoOfertas.domain.exception;

public class OfertaNoEncontradaException extends RuntimeException {
    public OfertaNoEncontradaException(String id) {
        super("No se encontró la oferta con ID: " + id);
    }
}
