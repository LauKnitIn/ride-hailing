package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

interface TarifaViajeJpaRepository extends JpaRepository<TarifaViajeJpaEntity, UUID> {
}
