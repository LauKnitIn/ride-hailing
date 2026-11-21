package com.ride_hailing.calificacion.application;

import java.util.List;
import java.util.UUID;

import com.ride_hailing.calificacion.domain.Calificacion;

public interface RepositorioCalificaciones {

    boolean existePorViaje(UUID viajeId);

    Calificacion guardar(Calificacion calificacion);

    List<Calificacion> buscarPorConductor(UUID conductorId);
}
