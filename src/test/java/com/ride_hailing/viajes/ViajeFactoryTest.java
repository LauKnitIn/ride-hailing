package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ViajeFactoryTest {

    private final ViajeFactory factory = new ViajeFactory();

    @Test
    void creaUnViajeSolicitadoValido() {
        UUID pasajeroId = UUID.randomUUID();
        Viaje viaje = factory.solicitar(pasajeroId, "Calle 1", "Calle 100");

        assertEquals(pasajeroId, viaje.getPasajeroId());
        assertEquals(EstadoViaje.Valor.SOLICITADO, viaje.getEstado().valor());
        assertNotNull(viaje.getId());
    }

    @Test
    void rechazaPasajeroNulo() {
        assertThrows(NullPointerException.class,
            () -> factory.solicitar(null, "Calle 1", "Calle 100"));
    }

    @Test
    void rechazaOrigenVacio() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.solicitar(UUID.randomUUID(), "  ", "Calle 100"));
    }

    @Test
    void rechazaOrigenIgualAlDestino() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 1"));
    }
}
