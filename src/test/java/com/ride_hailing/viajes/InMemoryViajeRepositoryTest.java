package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryViajeRepositoryTest {

    private final InMemoryViajeRepository repositorio = new InMemoryViajeRepository();
    private final ViajeFactory factory = new ViajeFactory();
    private final Ubicacion origen = new Ubicacion(5.5353, -73.3678);
    private final Ubicacion destino = new Ubicacion(5.5450, -73.3600);

    @Test
    void guardaYRecuperaPorId() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);

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
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);
        viaje.asignarConductor(conductorId, Instant.now());
        repositorio.guardar(viaje);

        assertEquals(1, repositorio.buscarPorConductorId(conductorId).size());
        assertTrue(repositorio.buscarPorConductorId(UUID.randomUUID()).isEmpty());
    }
}