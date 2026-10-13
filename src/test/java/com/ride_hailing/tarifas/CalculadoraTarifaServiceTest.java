package com.ride_hailing.tarifas;

import org.junit.jupiter.api.Test;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;


import static org.assertj.core.api.Assertions.*;

class CalculadoraTarifaServiceTest {

    private final CalculadoraTarifaService calculadora = new CalculadoraTarifaService();

    @Test
    void cargoParcialEsUnaFraccionDeLaTarifaMinima() {
        assertThat(calculadora.calcularCargoParcial(new Monto(5000), 0.5).valor()).isEqualTo(2500.0);
    }

    @Test
    void cargoParcialRechazaPorcentajesInvalidos() {
        assertThatThrownBy(() -> calculadora.calcularCargoParcial(new Monto(5000), 1.5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deberiaAplicarTarifaMinimaCuandoElCalculoEsMenor() {
        Monto tarifaMinima = new Monto(8.00);
        double valorKm = 2.00;
        double valorMinuto = 0.50;

        Monto resultado = calculadora.calcularMonto(
                1.0,
                2,
                tarifaMinima,
                valorKm,
                valorMinuto
        );

        assertThat(resultado.valor()).isEqualTo(8.00);
    }

    @Test
    void deberiaCalcularTarifaRealCuandoSuperaLaMinima() {
        Monto tarifaMinima = new Monto(8.00);
        double valorKm = 2.00;
        double valorMinuto = 0.50;

        Monto resultado = calculadora.calcularMonto(
                10.0,
                15,
                tarifaMinima,
                valorKm,
                valorMinuto
        );

        assertThat(resultado.valor()).isEqualTo(27.50);
    }
}