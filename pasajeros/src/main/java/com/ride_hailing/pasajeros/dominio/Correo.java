package com.ride_hailing.pasajeros.dominio;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record Correo(String valor) {

    private static final Pattern FORMATO_VALIDO =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    public Correo {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (!FORMATO_VALIDO.matcher(valor).matches()) {
            throw new IllegalArgumentException("El correo '" + valor + "' no tiene un formato válido");
        }
    }
}
