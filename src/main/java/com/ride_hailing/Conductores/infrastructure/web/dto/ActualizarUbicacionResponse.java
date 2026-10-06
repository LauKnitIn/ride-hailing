package com.ride_hailing.Conductores.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;

public record ActualizarUbicacionResponse() {
    public record ActualizarUbicacionRequest(
        @NotNull(message = "La latitud es obligatoria.")
        Double latitud,

        @NotNull(message = "La longitud es obligatoria.")
        Double longitud
    ) {}
}