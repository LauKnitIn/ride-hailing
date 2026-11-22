package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

public record SolicitudEmparejamientoRequest(
        String viajeId,
        double origenLatitud,
        double origenLongitud
) {}
