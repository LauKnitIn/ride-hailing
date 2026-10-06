package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ViajeFactoryTest {

    private final ViajeFactory factory = new ViajeFactory();
    private final Ubicacion origen = new Ubicacion(5.5353, -73.3678);
    private final Ubicacion destino = new Ubicacion(5.5450, -73.3600);

    @Test
    void creaUnViajeSolicitadoValido() {
        UUID pasajeroId = UUID.randomUUID();
        Viaje viaje = factory.solicitar(pasajeroId, origen, destino);

        assertEquals(pasajeroId, viaje.getPasajeroId());
        assertEquals(EstadoViaje.Valor.SOLICITADO, viaje.getEstado().valor());
        assertNotNull(viaje.getId());
    }

    @Test
    void rechazaPasajeroNulo() {
        assertThrows(NullPointerException.class,
            () -> factory.solicitar(null, origen, destino));
    }

    @Test
    void rechazaOrigenNulo() {
        assertThrows(NullPointerException.class,
            () -> factory.solicitar(UUID.randomUUID(), null, destino));
    }

    @Test
    void rechazaOrigenIgualAlDestino() {
        assertThrows(IllegalArgumentException.class,
            () -> factory.solicitar(UUID.randomUUID(), origen, origen));
    }
}