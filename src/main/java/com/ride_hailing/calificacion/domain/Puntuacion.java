package com.ride_hailing.calificacion.domain;

public record Puntuacion(int valor) {

    public Puntuacion {
        if (valor < 1 || valor > 5) {
            throw new IllegalArgumentException("La puntuación debe estar comprendida entre 1 y 5 estrellas.");
        }
    }

}
