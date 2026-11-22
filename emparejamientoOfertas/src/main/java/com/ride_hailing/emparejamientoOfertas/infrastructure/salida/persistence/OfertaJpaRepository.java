package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;

interface OfertaJpaRepository extends JpaRepository<OfertaJpaEntity, String> {

    List<OfertaJpaEntity> findByViajeId(String viajeId);

    boolean existsByViajeId(String viajeId);
    
    List<OfertaJpaEntity> findByConductorIdAndEstado(String conductorId, EstadoOferta estado);
}
