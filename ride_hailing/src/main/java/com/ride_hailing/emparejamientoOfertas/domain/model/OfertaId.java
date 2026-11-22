package com.ride_hailing.emparejamientoOfertas.domain.model;

import java.util.Objects;
import java.util.UUID;

public record OfertaId(String value) {
    public OfertaId {
        Objects.requireNonNull(value, "El valor de OfertaId no puede ser nulo");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor de OfertaId no puede estar vacío");
        }
    }

    public static OfertaId generar() {
        return new OfertaId(UUID.randomUUID().toString());
    }
}
