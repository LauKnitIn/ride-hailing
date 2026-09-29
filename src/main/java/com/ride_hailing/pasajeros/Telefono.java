package com.ride_hailing.pasajeros;

public record Telefono(String valor) {

    public Telefono {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (!valor.matches("\\d{7,15}")) {
            throw new IllegalArgumentException(
                "El teléfono '" + valor + "' debe tener solo dígitos (7 a 15)");
        }
    }
}
