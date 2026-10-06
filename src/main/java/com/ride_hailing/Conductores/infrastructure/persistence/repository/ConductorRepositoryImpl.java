package com.ride_hailing.Conductores.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.model.DriverId;
import com.ride_hailing.Conductores.domain.repository.ConductorRepository;
import com.ride_hailing.Conductores.infrastructure.persistence.entity.ConductorEntity;
import com.ride_hailing.Conductores.infrastructure.persistence.mapper.ConductorPersistenceMapper;

@Component
public class ConductorRepositoryImpl implements ConductorRepository {

    private final SpringDataConductorRepository jpaRepository;
    private final ConductorPersistenceMapper mapper;

    public ConductorRepositoryImpl(
            SpringDataConductorRepository jpaRepository,
            ConductorPersistenceMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Conductor save(Conductor conductor) {
        ConductorEntity entity = mapper.toEntity(conductor);
        ConductorEntity guardado = jpaRepository.save(entity);
        return mapper.toDomain(guardado);
    }

    @Override
    public Optional<Conductor> findById(DriverId driverId) {
        return jpaRepository.findById(driverId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Conductor> findByDocumentoIdentidad(DocumentoIdentidad documentoIdentidad) {
        return jpaRepository.findByDocumentoIdentidad(documentoIdentidad.getNumeroDocumento())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByDocumento(DocumentoIdentidad documento) {
        return jpaRepository.existsByNumeroDocumento(documento.getNumeroDocumento());
    }


}
