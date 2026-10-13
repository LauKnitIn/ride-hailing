package com.ride_hailing.tarifas.infraestructure.salida.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.tarifas.domain.TarifaCalculada;

public interface TarifaJpaRepository extends JpaRepository<TarifaCalculada, Long> {
}
