package com.ride_hailing.calificacion.infraestructure.salida.persistence;

import java.util.List;
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
    public List<Calificacion> buscarPorConductor(UUID conductorId) {
        return jpaRepository.findAllByConductorIdOrderByFechaCreacionDesc(conductorId)
                .stream()
                .map(this::aDominio)
                .toList();
    }

    @Override
    public Calificacion guardar(Calificacion calificacion) {
        try {
            return aDominio(jpaRepository.save(aEntidad(calificacion)));
        } catch (DataIntegrityViolationException exception) {
            throw new CalificacionDuplicadaException(calificacion.getViajeId());
        }
    }

    private CalificacionJpaEntity aEntidad(Calificacion calificacion) {
        CalificacionJpaEntity entidad = new CalificacionJpaEntity();
        entidad.id = calificacion.getId();
        entidad.viajeId = calificacion.getViajeId();
        entidad.pasajeroId = calificacion.getPasajeroId();
        entidad.conductorId = calificacion.getConductorId();
        entidad.puntuacion = calificacion.getPuntuacion().valor();
        entidad.comentario = calificacion.getComentario();
        entidad.fechaCreacion = calificacion.getFechaCreacion();
        return entidad;
    }

    private Calificacion aDominio(CalificacionJpaEntity entidad) {
        Calificacion calificacion = new Calificacion();
        calificacion.setId(entidad.id);
        calificacion.setViajeId(entidad.viajeId);
        calificacion.setPasajeroId(entidad.pasajeroId);
        calificacion.setConductorId(entidad.conductorId);
        calificacion.setPuntuacion(new Puntuacion(entidad.puntuacion));
        calificacion.setComentario(entidad.comentario);
        calificacion.setFechaCreacion(entidad.fechaCreacion);
        return calificacion;
    }
}
