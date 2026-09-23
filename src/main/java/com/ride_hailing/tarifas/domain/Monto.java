package com.ride_hailing.tarifas.domain;

public record Monto(double valor) {

    public Monto {
        if (valor < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo.");
        }
    }

    public double getValue() {
        return valor;
    }

}
