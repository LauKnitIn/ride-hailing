package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

public record OfertaResponse(
        String id,
        String viajeId,
        String conductorId,
        String estado
) {}
