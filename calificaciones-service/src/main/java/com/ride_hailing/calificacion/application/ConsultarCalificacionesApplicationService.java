package com.ride_hailing.calificacion.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.calificacion.domain.Calificacion;

@Service
public class ConsultarCalificacionesApplicationService implements ConsultarCalificacionesUseCase {

    private final RepositorioCalificaciones repositorioCalificaciones;

    public ConsultarCalificacionesApplicationService(RepositorioCalificaciones repositorioCalificaciones) {
        this.repositorioCalificaciones = repositorioCalificaciones;
    }

    @Override
    public List<Calificacion> consultarPorConductor(UUID conductorId) {
        return repositorioCalificaciones.buscarPorConductor(conductorId);
    }
}
