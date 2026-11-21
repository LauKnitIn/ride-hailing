package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.Test;

import com.ride_hailing.pasajeros.dominio.Telefono;

import static org.junit.jupiter.api.Assertions.*;

class TelefonoTest {

    @Test
    void aceptaUnNumeroValido() {
        Telefono telefono = new Telefono("3001234567");
        assertEquals("3001234567", telefono.valor());
    }

    @Test
    void rechazaTelefonoConLetras() {
        assertThrows(IllegalArgumentException.class, () -> new Telefono("300ABC4567"));
    }

    @Test
    void rechazaTelefonoDemasiadoCorto() {
        assertThrows(IllegalArgumentException.class, () -> new Telefono("123"));
    }

    @Test
    void rechazaTelefonoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Telefono(null));
    }
}
