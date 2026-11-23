package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.Test;

import com.ride_hailing.pasajeros.aplicacion.PasajeroFactory;
import com.ride_hailing.pasajeros.aplicacion.RegistroPasajeroService;
import com.ride_hailing.pasajeros.dominio.Pasajero;
import com.ride_hailing.pasajeros.infraestructura.salida.persistencia.InMemoryPasajeroRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryPasajeroRepositoryTest {

    private final InMemoryPasajeroRepository repositorio = new InMemoryPasajeroRepository();
    private final PasajeroFactory factory = new PasajeroFactory(new RegistroPasajeroService());

    @Test
    void guardaYRecuperaPorId() {
        Pasajero pasajero = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());

        repositorio.guardar(pasajero);

        assertEquals(pasajero, repositorio.buscarPorId(pasajero.getId()).orElseThrow());
    }

    @Test
    void listarTodosDevuelveTodosLosGuardados() {
        Pasajero p1 = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());
        Pasajero p2 = factory.registrar("Luis Gómez", "luis@correo.com", "3009876543", List.of(p1));

        repositorio.guardar(p1);
        repositorio.guardar(p2);

        assertEquals(2, repositorio.listarTodos().size());
    }

    @Test
    void buscarPorIdInexistenteDevuelveVacio() {
        assertTrue(repositorio.buscarPorId(UUID.randomUUID()).isEmpty());
    }
}
