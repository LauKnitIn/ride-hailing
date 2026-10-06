package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransicionEstadoViajeServiceTest {

    private final ViajeFactory factory = new ViajeFactory();
    private final TransicionEstadoViajeService service = new TransicionEstadoViajeService();
    private final Ubicacion origen = new Ubicacion(5.5353, -73.3678);
    private final Ubicacion destino = new Ubicacion(5.5450, -73.3600);

    @Test
    void asignaConductorSiNoTieneViajesEnCurso() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);
        UUID conductorId = UUID.randomUUID();

        service.asignarConductor(viaje, conductorId, List.of(), Instant.now());

        assertEquals(EstadoViaje.Valor.ASIGNADO, viaje.getEstado().valor());
        assertEquals(conductorId, viaje.getConductorId());
    }

    @Test
    void noAsignaConductorConUnViajeEnCurso() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);
        UUID conductorId = UUID.randomUUID();

        Viaje otroViajeEnCurso = factory.solicitar(UUID.randomUUID(), origen, destino);
        service.asignarConductor(otroViajeEnCurso, conductorId, List.of(), Instant.now());
        otroViajeEnCurso.iniciar(Instant.now());

        assertThrows(ConductorNoDisponibleException.class,
            () -> service.asignarConductor(viaje, conductorId, List.of(otroViajeEnCurso), Instant.now()));
    }

    @Test
    void cancelarAntesDeAsignarNoGeneraCargoParcial() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            service.cancelar(viaje, "El pasajero cambió de planes");

        assertFalse(resultado.requiereCargoParcial());
        assertEquals(EstadoViaje.Valor.CANCELADO, viaje.getEstado().valor());
    }

    @Test
    void cancelarDespuesDeAsignadoSiGeneraCargoParcial() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), origen, destino);
        service.asignarConductor(viaje, UUID.randomUUID(), List.of(), Instant.now());

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            service.cancelar(viaje, "El conductor tuvo un imprevisto");

        assertTrue(resultado.requiereCargoParcial());
    }
}