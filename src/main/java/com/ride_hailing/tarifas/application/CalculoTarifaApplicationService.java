package com.ride_hailing.tarifas.application;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;

public class CalculoTarifaApplicationService
        implements CalculoTarifaUseCase {

    private final CalculadoraTarifaService calculadoraTarifaService;

    public CalculoTarifaApplicationService(
            CalculadoraTarifaService calculadoraTarifaService) {
        this.calculadoraTarifaService = calculadoraTarifaService;
    }

    @Override
    public Monto calcularTarifa(
            double distanciaKm,
            double tiempoMinutos,
            Monto tarifaMinima,
            double tarifaPorKm,
            double tarifaPorMinuto) {

        return calculadoraTarifaService.calcularTarifa(
                distanciaKm,
                tiempoMinutos,
                tarifaMinima,
                tarifaPorKm,
                tarifaPorMinuto
        );
    }
}
