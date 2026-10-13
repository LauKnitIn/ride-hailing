package com.ride_hailing.calificacion.application;

import com.ride_hailing.calificacion.domain.Calificacion;

public interface RepositorioCalificaciones {

    Calificacion guardar(Calificacion calificacion);
}
