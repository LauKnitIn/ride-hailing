package com.ride_hailing.calificacion.infraestructure.salida.persistencia;

import org.springframework.stereotype.Repository;

import com.ride_hailing.calificacion.application.RepositorioCalificaciones;
import com.ride_hailing.calificacion.domain.Calificacion;

@Repository
public class CalificacionPersistenceAdapter implements RepositorioCalificaciones {

    private final CalificacionJpaRepository repository;

    public CalificacionPersistenceAdapter(CalificacionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Calificacion guardar(Calificacion calificacion) {
        return repository.save(calificacion);
    }
}
