package com.ride_hailing.calificacion.application;

import com.ride_hailing.calificacion.domain.Calificacion;
import com.ride_hailing.calificacion.domain.ClasificacionFactory;
import com.ride_hailing.calificacion.domain.Puntuacion;
import org.springframework.stereotype.Service;

@Service
public class CalificarConductorApplicationService
        implements CalificarConductorUseCase {

    private final RepositorioViajes repositorioViajes;
    private final RepositorioCalificaciones repositorioCalificaciones;

    public CalificarConductorApplicationService(
            RepositorioViajes repositorioViajes,
            RepositorioCalificaciones repositorioCalificaciones
    ) {
        this.repositorioViajes = repositorioViajes;
        this.repositorioCalificaciones = repositorioCalificaciones;
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

        Calificacion calificacion = ClasificacionFactory
                .crearCalificacion(
                        viajeId,
                        viaje.pasajeroId(),
                        viaje.conductorId(),
                        new Puntuacion(puntuacion),
                        comentario
                );
        return repositorioCalificaciones.guardar(calificacion);
    }
}