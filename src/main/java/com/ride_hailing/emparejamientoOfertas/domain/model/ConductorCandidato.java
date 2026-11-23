package com.ride_hailing.emparejamientoOfertas.domain.model;

import java.util.Objects;

public record ConductorCandidato(String conductorId,double latitud, double longitud) {
    public ConductorCandidato {
        Objects.requireNonNull(conductorId, "El conductorId no puede ser nulo");
        if (conductorId.trim().isEmpty()) {
            throw new IllegalArgumentException("El conductorId no puede estar vacío");
        }
    }
}
