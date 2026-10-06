package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface OfertaJpaRepository extends JpaRepository<OfertaJpaEntity, String> {
    List<OfertaJpaEntity> findByViajeId(String viajeId);
}
