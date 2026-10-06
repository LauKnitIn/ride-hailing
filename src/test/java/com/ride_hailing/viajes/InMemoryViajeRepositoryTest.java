package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryViajeRepositoryTest {

    private final InMemoryViajeRepository repositorio = new InMemoryViajeRepository();
    private final ViajeFactory factory = new ViajeFactory();

    @Test
    void guardaYRecuperaPorId() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");

        repositorio.guardar(viaje);

        assertEquals(viaje, repositorio.buscarPorId(viaje.getId()).orElseThrow());
    }

    @Test
    void buscarPorIdInexistenteDevuelveVacio() {
        assertTrue(repositorio.buscarPorId(UUID.randomUUID()).isEmpty());
    }

    @Test
    void buscaPorConductorId() {
        UUID conductorId = UUID.randomUUID();
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        viaje.asignarConductor(conductorId, java.time.LocalDateTime.now());
        repositorio.guardar(viaje);

        assertEquals(1, repositorio.buscarPorConductorId(conductorId).size());
        assertTrue(repositorio.buscarPorConductorId(UUID.randomUUID()).isEmpty());
    }
}
