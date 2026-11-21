package com.ride_hailing.tarifas.infraestructure.entrada.web;

import java.util.UUID;

import com.ride_hailing.tarifas.domain.Monto;

public record TarifaResponse(
        UUID viajeId,
        double monto
) {
    public static TarifaResponse desde(UUID viajeId, Monto monto) {
        return new TarifaResponse(viajeId, monto.valor());
    }
}
