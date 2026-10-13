package com.ride_hailing.calificacion.application;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ride_hailing.calificacion.domain.Calificacion;

public class RepositorioCalificacionesFake implements RepositorioCalificaciones {

    private final Map<UUID, Calificacion> porViaje = new HashMap<>();

    @Override
    public boolean existePorViaje(UUID viajeId) {
        return porViaje.containsKey(viajeId);
    }

    @Override
    public Calificacion guardar(Calificacion calificacion) {
        porViaje.put(calificacion.getViajeId(), calificacion);
        return calificacion;
    }
}
