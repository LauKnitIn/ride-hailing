package com.ride_hailing.calificacion.infraestructure.salida.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface CalificacionJpaRepository extends JpaRepository<CalificacionJpaEntity, UUID> {

    boolean existsByViajeId(UUID viajeId);
}
