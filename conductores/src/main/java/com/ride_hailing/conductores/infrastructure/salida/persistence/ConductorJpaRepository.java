package com.ride_hailing.conductores.infrastructure.salida.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;
import com.ride_hailing.conductores.domain.model.TipoDocumento;

interface ConductorJpaRepository extends JpaRepository<ConductorJpaEntity, String> {

    boolean existsByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    List<ConductorJpaEntity> findByEstado(EstadoDisponibilidad estado);
}
