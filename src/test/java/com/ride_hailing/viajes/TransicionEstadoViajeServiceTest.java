package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransicionEstadoViajeServiceTest {

    private final ViajeFactory factory = new ViajeFactory();
    private final TransicionEstadoViajeService service = new TransicionEstadoViajeService();

    @Test
    void asignaConductorSiNoTieneViajesEnCurso() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        UUID conductorId = UUID.randomUUID();

        service.asignarConductor(viaje, conductorId, List.of(), LocalDateTime.now());

        assertEquals(EstadoViaje.Valor.ASIGNADO, viaje.getEstado().valor());
        assertEquals(conductorId, viaje.getConductorId());
    }

    @Test
    void noAsignaConductorConUnViajeEnCurso() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        UUID conductorId = UUID.randomUUID();

        Viaje otroViajeEnCurso = factory.solicitar(UUID.randomUUID(), "Carrera 5", "Carrera 50");
        service.asignarConductor(otroViajeEnCurso, conductorId, List.of(), LocalDateTime.now());
        otroViajeEnCurso.iniciar();

        assertThrows(ConductorNoDisponibleException.class,
            () -> service.asignarConductor(viaje, conductorId, List.of(otroViajeEnCurso), LocalDateTime.now()));
    }

    @Test
    void cancelarAntesDeAsignarNoGeneraCargoParcial() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            service.cancelar(viaje, "El pasajero cambió de planes");

        assertFalse(resultado.requiereCargoParcial());
        assertEquals(EstadoViaje.Valor.CANCELADO, viaje.getEstado().valor());
    }

    @Test
    void cancelarDespuesDeAsignadoSiGeneraCargoParcial() {
        Viaje viaje = factory.solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        service.asignarConductor(viaje, UUID.randomUUID(), List.of(), LocalDateTime.now());

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            service.cancelar(viaje, "El conductor tuvo un imprevisto");

        assertTrue(resultado.requiereCargoParcial());
    }
}
