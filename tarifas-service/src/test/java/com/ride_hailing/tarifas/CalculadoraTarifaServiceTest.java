package com.ride_hailing.tarifas;

import org.junit.jupiter.api.Test;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void aplicaTarifaMinimaCuandoElCalculoEsMenor() {
        Monto resultado = calculadora.calcularMonto(1.0, 2, new Monto(8.00), 2.00, 0.50);
        assertThat(resultado.valor()).isEqualTo(8.00);
    }

    @Test
    void calculaTarifaRealCuandoSuperaLaMinima() {
        Monto resultado = calculadora.calcularMonto(10.0, 15, new Monto(8.00), 2.00, 0.50);
        assertThat(resultado.valor()).isEqualTo(27.50);
    }
}
