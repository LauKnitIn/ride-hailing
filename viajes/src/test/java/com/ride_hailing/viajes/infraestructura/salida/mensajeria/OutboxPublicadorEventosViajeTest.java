package com.ride_hailing.viajes.infraestructura.salida.mensajeria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeSolicitadoEvent;

import tools.jackson.databind.json.JsonMapper;

class OutboxPublicadorEventosViajeTest {

    private final OutboxEventRepository repositorio = mock(OutboxEventRepository.class);
    private final OutboxPublicadorEventosViaje publicador =
            new OutboxPublicadorEventosViaje(repositorio, JsonMapper.builder().build());

    @Test
    void guardaElViajeSolicitadoEnSuTopicoConElViajeComoClave() {
        UUID viajeId = UUID.randomUUID();

        publicador.publicar(new ViajeSolicitadoEvent(viajeId, UUID.randomUUID(), 4.711, -74.0721, Instant.now()));

        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(repositorio).save(captor.capture());
        assertEquals("viajes.solicitados", captor.getValue().getTopico());
        assertEquals(viajeId.toString(), captor.getValue().getClave());
        assertTrue(captor.getValue().getPayload().contains("\"latitudOrigen\":4.711"));
        assertFalse(captor.getValue().isPublicado());
    }

    @Test
    void unaCancelacionSinConductorSeGuardaConConductorNulo() {
        publicador.publicar(new ViajeCanceladoEvent(UUID.randomUUID(), null, false, Instant.now()));

        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(repositorio).save(captor.capture());
        assertEquals("viajes.cancelados", captor.getValue().getTopico());
        assertTrue(captor.getValue().getPayload().contains("\"conductorId\":null"));
    }

    @Test
    void unEventoDesconocidoNoSeGuarda() {
        assertThrows(IllegalArgumentException.class, () -> publicador.publicar("no soy un evento"));
        verifyNoInteractions(repositorio);
    }
}