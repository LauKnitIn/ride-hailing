package com.ride_hailing.tarifas.infraestructure.entrada.mensajeria;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.application.DatosViajeTarifa;

import tools.jackson.databind.ObjectMapper;

class ViajesKafkaConsumerTest {

    private CalculoTarifaUseCase calculoTarifa;
    private ViajesKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        calculoTarifa = mock(CalculoTarifaUseCase.class);
        consumer = new ViajesKafkaConsumer(new ObjectMapper(), calculoTarifa);
    }

    @Test
    void alFinalizarRegistraDatosYCalculaLaTarifaDelViaje() {
        UUID viajeId = UUID.randomUUID();

        consumer.alFinalizarViaje("""
                {"viajeId":"%s","distanciaKm":10.0,"duracionMinutos":20.0,"momento":"2026-11-21T10:00:00Z"}
                """.formatted(viajeId));

        verify(calculoTarifa).registrarViajeFinalizado(viajeId, new DatosViajeTarifa(10.0, 20.0));
    }

    @Test
    void alCancelarSoloRegistraCargoParcialCuandoElEventoLoSolicita() {
        UUID viajeId = UUID.randomUUID();

        consumer.alCancelarViaje("""
                {"viajeId":"%s","requiereCargoParcial":true,"momento":"2026-11-21T10:00:00Z"}
                """.formatted(viajeId));

        verify(calculoTarifa).registrarCargoParcial(viajeId);
    }

    @Test
    void alCancelarSinCargoParcialNoModificaLaTarifa() {
        UUID viajeId = UUID.randomUUID();

        consumer.alCancelarViaje("""
                {"viajeId":"%s","requiereCargoParcial":false,"momento":"2026-11-21T10:00:00Z"}
                """.formatted(viajeId));

        verifyNoInteractions(calculoTarifa);
    }

    @Test
    void rechazaEventosFinalizadosConDistanciaNegativa() {
        UUID viajeId = UUID.randomUUID();

        assertThatThrownBy(() -> consumer.alFinalizarViaje("""
                {"viajeId":"%s","distanciaKm":-1.0,"duracionMinutos":20.0}
                """.formatted(viajeId)))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(calculoTarifa);
    }

    @Test
    void rechazaUnIdentificadorInvalidoInclusoSiLaCancelacionNoGeneraCargo() {
        assertThatThrownBy(() -> consumer.alCancelarViaje("""
                {"viajeId":"invalido","requiereCargoParcial":false}
                """))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(calculoTarifa);
    }
}
