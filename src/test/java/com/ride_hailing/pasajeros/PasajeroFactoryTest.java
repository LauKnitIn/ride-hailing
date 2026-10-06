package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.Test;

import com.ride_hailing.pasajeros.aplicacion.PasajeroFactory;
import com.ride_hailing.pasajeros.aplicacion.RegistroPasajeroService;
import com.ride_hailing.pasajeros.dominio.CorreoYaRegistradoException;
import com.ride_hailing.pasajeros.dominio.Pasajero;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasajeroFactoryTest {

    private final PasajeroFactory factory = new PasajeroFactory(new RegistroPasajeroService());

    @Test
    void registraUnPasajeroValido() {
        Pasajero pasajero = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());

        assertEquals("Ana Pérez", pasajero.getNombre());
        assertNotNull(pasajero.getId());
    }

    @Test
    void rechazaNombreVacio() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.registrar("  ", "ana@correo.com", "3001234567", List.of()));
    }

    @Test
    void rechazaCorreoConFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.registrar("Ana Pérez", "no-es-un-correo", "3001234567", List.of()));
    }

    @Test
    void rechazaCorreoDuplicado() {
        Pasajero existente = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());

        assertThrows(CorreoYaRegistradoException.class,
            () -> factory.registrar("Otra Ana", "ana@correo.com", "3009876543", List.of(existente)));
    }
}
