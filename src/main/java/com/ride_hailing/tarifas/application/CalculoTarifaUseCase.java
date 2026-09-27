package com.ride_hailing.tarifas.application;

import com.ride_hailing.tarifas.domain.Monto;

public interface CalculoTarifaUseCase {

    Monto calcularTarifa(
            double distanciaKm,
            double tiempoMinutos,
            Monto tarifaMinima,
            double tarifaPorKm,
            double tarifaPorMinuto
    );
}