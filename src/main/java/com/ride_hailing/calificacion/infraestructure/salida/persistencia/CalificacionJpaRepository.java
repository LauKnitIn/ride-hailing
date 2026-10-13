package com.ride_hailing.calificacion.infraestructure.salida.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.calificacion.domain.Calificacion;

public interface CalificacionJpaRepository extends JpaRepository<Calificacion, Long> {
}
