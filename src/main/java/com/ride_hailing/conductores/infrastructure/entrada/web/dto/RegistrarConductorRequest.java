package com.ride_hailing.conductores.infrastructure.entrada.web.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegistrarConductorRequest(
        @NotBlank String nombreCompleto,
        @NotBlank String tipoDocumento,
        @NotBlank String numeroDocumento,
        @NotNull @Past LocalDate fechaNacimiento) {}
