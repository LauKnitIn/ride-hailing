package com.ride_hailing.calificacion.application;

import com.ride_hailing.calificacion.domain.Calificacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalificarConductorApplicationServiceConFakeTest {

    @Test
    void calificaConductorSinInfraestructura() {

        RepositorioViajes repositorio =
                new RepositorioViajesFake();

        CalificarConductorApplicationService service =
                new CalificarConductorApplicationService(
                        repositorio
                );

        Calificacion calificacion =
                service.calificarConductor(
                        1L,
                        5,
                        "Excelente servicio"
                );

        assertEquals(
                1L,
                calificacion.getViajeId()
        );

        assertEquals(
                100L,
                calificacion.getPasajeroId()
        );

        assertEquals(
                200L,
                calificacion.getConductorId()
        );

        assertEquals(
                5,
                calificacion.getPuntuacion().valor()
        );

        assertEquals(
                "Excelente servicio",
                calificacion.getComentario()
        );

        assertNotNull(
                calificacion.getFechaCreacion()
        );
    }
}