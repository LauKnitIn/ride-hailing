package com.ride_hailing.Conductores.infrastructure.web.dto;

public record ConductorResponse(
    String idConductor,
    String tipoDocumento,
    String numeroDocumento,
    String nombreCompleto,
    String estadoDisponibilidad,
    Double latitudActual,
    Double longitudActual) {}
