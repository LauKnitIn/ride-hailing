package com.ride_hailing.calificacion.application;

import java.util.List;
import java.util.UUID;

import com.ride_hailing.calificacion.domain.Calificacion;

public interface ConsultarCalificacionesUseCase {

    List<Calificacion> consultarPorConductor(UUID conductorId);
}
