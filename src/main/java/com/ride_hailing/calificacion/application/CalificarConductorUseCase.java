package com.ride_hailing.calificacion.application;

import java.util.UUID;

import com.ride_hailing.calificacion.domain.Calificacion;

public interface CalificarConductorUseCase {

    Calificacion calificarConductor(UUID viajeId, UUID pasajeroId, int puntuacion, String comentario);

}
