package com.ride_hailing.tarifas.infraestructure.entrada.mensajeria;

public record ViajeCanceladoMessage(
        String viajeId,
        Boolean requiereCargoParcial
) {
}
