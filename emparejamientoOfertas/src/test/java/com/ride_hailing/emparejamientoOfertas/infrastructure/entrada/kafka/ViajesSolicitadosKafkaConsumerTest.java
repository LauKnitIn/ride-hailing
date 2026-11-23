package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

import tools.jackson.databind.ObjectMapper;

class ViajesSolicitadosKafkaConsumerTest {

    private ProcesarSolicitudEmparejamientoUseCase procesar;
    private OfertaRepositoryPort ofertaRepository;
    private ViajesSolicitadosKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        procesar = mock(ProcesarSolicitudEmparejamientoUseCase.class);
        ofertaRepository = mock(OfertaRepositoryPort.class);
        consumer = new ViajesSolicitadosKafkaConsumer(procesar, ofertaRepository, new ObjectMapper());
    }

    @Test
    void procesaUnViajeSolicitado() {
        when(ofertaRepository.existePorViajeId(any(ViajeId.class))).thenReturn(false);
        String payload = "{\"viajeId\":\"v-1\",\"pasajeroId\":\"p-1\",\"latitudOrigen\":4.0,\"longitudOrigen\":-73.0,\"momento\":\"2026-01-01T00:00:00Z\"}";

        consumer.consumir(payload);

        verify(procesar).procesar(eq("v-1"), eq(4.0), eq(-73.0));
    }

    @Test
    void noReprocesaSiElViajeYaTieneOfertas() {
        when(ofertaRepository.existePorViajeId(any(ViajeId.class))).thenReturn(true);
        String payload = "{\"viajeId\":\"v-1\",\"pasajeroId\":\"p-1\",\"latitudOrigen\":4.0,\"longitudOrigen\":-73.0,\"momento\":\"2026-01-01T00:00:00Z\"}";

        consumer.consumir(payload);

        verify(procesar, never()).procesar(anyString(), anyDouble(), anyDouble());
    }

    @Test
    void noFallaConPayloadInvalido() {
        consumer.consumir("{no-es-json");
        verify(procesar, never()).procesar(anyString(), anyDouble(), anyDouble());
    }
}
