package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorreoTest {

    @Test
    void aceptaUnCorreoConFormatoValido() {
        Correo correo = new Correo("ana.perez@correo.com");
        assertEquals("ana.perez@correo.com", correo.valor());
    }

    @Test
    void rechazaCorreoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Correo(null));
    }

    @Test
    void rechazaCorreoVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Correo("  "));
    }

    @Test
    void rechazaCorreoSinArroba() {
        assertThrows(IllegalArgumentException.class, () -> new Correo("ana.perez.correo.com"));
    }

    @Test
    void dosCorreosConElMismoValorSonIguales() {
        assertEquals(new Correo("a@b.com"), new Correo("a@b.com"));
    }
}
