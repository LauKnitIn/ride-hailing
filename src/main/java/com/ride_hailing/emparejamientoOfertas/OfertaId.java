package com.ride_hailing.emparejamientoOfertas;

import java.util.Objects;
import java.util.UUID;

public record OfertaId(UUID value) {
    public OfertaId {
        Objects.requireNonNull(value, "El ID de la oferta no puede ser nulo");
    }

    public static OfertaId generar() {
        return new OfertaId(UUID.randomUUID());
    }
}
