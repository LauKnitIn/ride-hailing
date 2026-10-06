package com.ride_hailing.emparejamientoOfertas.domain.model;

import java.util.Objects;

public record ViajeId(String value) {
    public ViajeId {
            Objects.requireNonNull(value, "El valor de ViajeId no puede ser nulo");
            if (value.trim().isEmpty()) {
                throw new IllegalArgumentException("El valor de ViajeId no puede estar vacío");
            }
    }
}
