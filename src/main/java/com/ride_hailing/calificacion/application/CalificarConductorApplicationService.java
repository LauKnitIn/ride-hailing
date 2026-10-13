package com.ride_hailing.calificacion.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.calificacion.domain.Calificacion;
import com.ride_hailing.calificacion.domain.CalificacionDuplicadaException;
import com.ride_hailing.calificacion.domain.ClasificacionFactory;
import com.ride_hailing.calificacion.domain.PasajeroNoAutorizadoException;
import com.ride_hailing.calificacion.domain.Puntuacion;
import com.ride_hailing.calificacion.domain.ViajeNoCalificableException;

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
            UUID viajeId,
            UUID pasajeroId,
            int puntuacion,
            String comentario
    ) {
        Puntuacion puntuacionValida = new Puntuacion(puntuacion);
        DatosViajeCalificacion viaje = repositorioViajes.buscarPorId(viajeId);

        if (pasajeroId == null || !pasajeroId.equals(viaje.pasajeroId())) {
            throw new PasajeroNoAutorizadoException(pasajeroId, viajeId);
        }
        if (!viaje.finalizado() || viaje.conductorId() == null) {
            throw new ViajeNoCalificableException(
                    "Solo se puede calificar un viaje finalizado con conductor asignado");
        }
        if (repositorioCalificaciones.existePorViaje(viajeId)) {
            throw new CalificacionDuplicadaException(viajeId);
        }

        Calificacion calificacion = ClasificacionFactory.crearCalificacion(
                viajeId,
                viaje.pasajeroId(),
                viaje.conductorId(),
                puntuacionValida,
                comentario);

        return repositorioCalificaciones.guardar(calificacion);
    }
}