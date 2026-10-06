package com.ride_hailing.calificacion.application;

import com.ride_hailing.calificacion.domain.Calificacion;
import com.ride_hailing.calificacion.domain.ClasificacionFactory;
import com.ride_hailing.calificacion.domain.Puntuacion;
import org.springframework.stereotype.Service;

@Service
public class CalificarConductorApplicationService
        implements CalificarConductorUseCase {

    private final RepositorioViajes repositorioViajes;

    public CalificarConductorApplicationService(
            RepositorioViajes repositorioViajes
    ) {
        this.repositorioViajes = repositorioViajes;
    }

    @Override
    public Calificacion calificarConductor(
            Long viajeId,
            int puntuacion,
            String comentario
    ) {

        DatosViajeCalificacion viaje =
                repositorioViajes.buscarPorId(
                        viajeId
                );

        return ClasificacionFactory
                .crearCalificacion(
                        viajeId,
                        viaje.pasajeroId(),
                        viaje.conductorId(),
                        new Puntuacion(puntuacion),
                        comentario
                );
    }
}