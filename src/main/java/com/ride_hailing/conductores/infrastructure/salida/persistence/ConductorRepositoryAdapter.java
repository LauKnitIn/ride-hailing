package com.ride_hailing.conductores.infrastructure.salida.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.salida.ConductorRepositoryPort;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;

@Component
public class ConductorRepositoryAdapter implements ConductorRepositoryPort {

    private final ConductorJpaRepository jpaRepository;
    private final ConductorPersistenceMapper mapper;

    ConductorRepositoryAdapter(ConductorJpaRepository jpaRepository, ConductorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Conductor> buscarPorId(DriverId id) {
        return jpaRepository.findById(String.valueOf(id.value())).map(mapper::aDominio);
    }

    @Override
    public List<Conductor> buscarPorEstado(EstadoDisponibilidad estado) {
        return jpaRepository.findByEstado(estado).stream().map(mapper::aDominio).toList();
    }

    @Override
    public boolean existeDocumento(DocumentoIdentidad documento) {
        return jpaRepository.existsByTipoDocumentoAndNumeroDocumento(
                mapper.aTipoDocumento(documento.getTipoDocumento()),     // ← String → enum
                documento.getNumeroDocumento());
    }

    @Override
    public Conductor guardar(Conductor conductor) {
        return mapper.aDominio(jpaRepository.save(mapper.aEntidad(conductor)));
    }
}

