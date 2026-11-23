package com.ride_hailing.tarifas.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;

class CalculoTarifaApplicationServiceConFakeTest {

    @Test
    void calculaTarifaSinInfraestructura() {

        RepositorioViajes repositorio =
                new RepositorioViajesFake();

        CalculadoraTarifaService calculadora =
                new CalculadoraTarifaService();

        CalculoTarifaApplicationService service =
                new CalculoTarifaApplicationService(
                        repositorio,
                        calculadora
                );

        Monto tarifa =
                service.calcularTarifa(UUID.randomUUID());

        assertEquals(
                21000.0,
                tarifa.getValue()
        );
    }
}