package com.ride_hailing.Conductores.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.ride_hailing.Conductores.infrastructure.persistence.entity.ConductorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository 
public interface SpringDataConductorRepository extends JpaRepository<ConductorEntity, String>{

    Optional<ConductorEntity> findByNumeroDocumento (String numeroDocumento);
    boolean existsByNumeroDocumento(String numeroDocumento);

}
