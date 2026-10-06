package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers.dto;

public record RespuestaOfertaRequest(
        boolean aceptada,
        double origenLat,
        double origenLon
) {

}
