package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers.dto;

import java.util.List;
import java.util.UUID;

import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;

public record SolicitudEmparejamientoRequest(
        UUID viajeId,
        double origenLat,
        double origenLon,
        List<CandidatoConductor> candidatos,
        List<UUID> rechazados,
        double radioKm
) {}
