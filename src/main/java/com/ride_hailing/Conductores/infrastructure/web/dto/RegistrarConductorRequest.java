package com.ride_hailing.Conductores.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;


public record RegistrarConductorRequest(
        @NotBlank(message = "El tipo de documento es obligatorio.")
        String tipoDocumento,

        @NotBlank(message = "El número de documento es obligatorio.")
        String numeroDocumento,

        @NotBlank(message = "El nombre completo es obligatorio.")
        String nombreCompleto,

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
        LocalDate fechaNacimiento
) {}
