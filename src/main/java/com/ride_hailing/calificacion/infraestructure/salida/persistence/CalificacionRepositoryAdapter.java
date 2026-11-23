package com.ride_hailing.calificacion.infraestructure.salida.persistence;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.ride_hailing.calificacion.application.RepositorioCalificaciones;
import com.ride_hailing.calificacion.domain.Calificacion;
import com.ride_hailing.calificacion.domain.CalificacionDuplicadaException;
import com.ride_hailing.calificacion.domain.Puntuacion;

@Component
class CalificacionRepositoryAdapter implements RepositorioCalificaciones {

    private final CalificacionJpaRepository jpaRepository;

    CalificacionRepositoryAdapter(CalificacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existePorViaje(UUID viajeId) {
        return jpaRepository.existsByViajeId(viajeId);
    }

    @Override
    public Calificacion guardar(Calificacion calificacion) {
        try {
            return aDominio(jpaRepository.save(aEntidad(calificacion)));
        } catch (DataIntegrityViolationException e) {
            // Dos calificaciones simultaneas del mismo viaje: gana la primera (UNIQUE sobre viajeId).
            throw new CalificacionDuplicadaException(calificacion.getViajeId());
        }
    }

    private CalificacionJpaEntity aEntidad(Calificacion c) {
        CalificacionJpaEntity e = new CalificacionJpaEntity();
        e.id = c.getId();
        e.viajeId = c.getViajeId();
        e.pasajeroId = c.getPasajeroId();
        e.conductorId = c.getConductorId();
        e.puntuacion = c.getPuntuacion().valor();
        e.comentario = c.getComentario();
        e.fechaCreacion = c.getFechaCreacion();
        return e;
    }

    private Calificacion aDominio(CalificacionJpaEntity e) {
        Calificacion c = new Calificacion();
        c.setId(e.id);
        c.setViajeId(e.viajeId);
        c.setPasajeroId(e.pasajeroId);
        c.setConductorId(e.conductorId);
        c.setPuntuacion(new Puntuacion(e.puntuacion));
        c.setComentario(e.comentario);
        c.setFechaCreacion(e.fechaCreacion);
        return c;
    }
}
