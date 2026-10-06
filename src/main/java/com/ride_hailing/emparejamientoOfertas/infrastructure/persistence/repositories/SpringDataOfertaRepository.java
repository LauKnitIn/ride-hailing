package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities.OfertaEntity;

public interface SpringDataOfertaRepository extends JpaRepository<OfertaEntity, UUID>{
    List<OfertaEntity> findByViajeId(UUID viajeId);

}
