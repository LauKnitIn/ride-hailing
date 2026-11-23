package com.ride_hailing.conductores.infrastructure.entrada.web.dto;

import java.time.LocalDate;

public record ConductorResponse(String id, String nombreCompleto, String tipoDocumento, String numeroDocumento,
                                LocalDate fechaNacimiento, String disponibilidad, Double latitud, Double longitud) {}
