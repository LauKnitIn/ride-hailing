package com.ride_hailing.tarifas.infraestructure.entrada.mensajeria;

public record ViajeFinalizadoMessage(
        String viajeId,
        Double distanciaKm,
        Double duracionMinutos
) {
}
