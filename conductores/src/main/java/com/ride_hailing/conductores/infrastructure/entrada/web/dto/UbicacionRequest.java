package com.ride_hailing.conductores.infrastructure.entrada.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record UbicacionRequest(
        @DecimalMin("-90.0") @DecimalMax("90.0") double latitud,
        @DecimalMin("-180.0") @DecimalMax("180.0") double longitud) {}