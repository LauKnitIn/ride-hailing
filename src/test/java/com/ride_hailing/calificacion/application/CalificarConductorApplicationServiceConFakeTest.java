package com.ride_hailing.calificacion.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.ride_hailing.calificacion.domain.Calificacion;
import com.ride_hailing.calificacion.domain.CalificacionDuplicadaException;
import com.ride_hailing.calificacion.domain.PasajeroNoAutorizadoException;
import com.ride_hailing.calificacion.domain.ViajeNoCalificableException;

class CalificarConductorApplicationServiceConFakeTest {

    private static final UUID PASAJERO = RepositorioViajesFake.PASAJERO;

    private CalificarConductorApplicationService servicio(boolean viajeFinalizado) {
        return new CalificarConductorApplicationService(
                new RepositorioViajesFake(viajeFinalizado), new RepositorioCalificacionesFake());
    }

    @Test
    void calificaConductorSinInfraestructura() {
        UUID viajeId = UUID.randomUUID();

        Calificacion calificacion = servicio(true).calificarConductor(viajeId, PASAJERO, 5, "Excelente servicio");

        assertEquals(viajeId, calificacion.getViajeId());
        assertEquals(PASAJERO, calificacion.getPasajeroId());
        assertEquals(RepositorioViajesFake.CONDUCTOR, calificacion.getConductorId());
        assertEquals(5, calificacion.getPuntuacion().valor());
        assertEquals("Excelente servicio", calificacion.getComentario());
        assertNotNull(calificacion.getId());
        assertNotNull(calificacion.getFechaCreacion());
    }

    @Test
    void noPermiteCalificarDosVecesElMismoViaje() {
        CalificarConductorApplicationService service = servicio(true);
        UUID viajeId = UUID.randomUUID();
        service.calificarConductor(viajeId, PASAJERO, 4, "ok");

        assertThrows(CalificacionDuplicadaException.class,
                () -> service.calificarConductor(viajeId, PASAJERO, 5, "otra vez"));
    }

    @Test
    void noPermiteCalificarUnViajeNoFinalizado() {
        assertThrows(ViajeNoCalificableException.class,
                () -> servicio(false).calificarConductor(UUID.randomUUID(), PASAJERO, 5, "x"));
    }

    @Test
    void soloElPasajeroDelViajePuedeCalificar() {
        assertThrows(PasajeroNoAutorizadoException.class,
                () -> servicio(true).calificarConductor(UUID.randomUUID(), UUID.randomUUID(), 5, "x"));
        assertThrows(PasajeroNoAutorizadoException.class,
                () -> servicio(true).calificarConductor(UUID.randomUUID(), null, 5, "x"));
    }
}
