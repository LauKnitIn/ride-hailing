package com.ride_hailing.calificacion.application;
import com.ride_hailing.calificacion.domain.Calificacion;

public interface CalificarConductorUseCase {

    Calificacion calificarConductor(Long viajeId, int puntuacion, String comentario);

}
