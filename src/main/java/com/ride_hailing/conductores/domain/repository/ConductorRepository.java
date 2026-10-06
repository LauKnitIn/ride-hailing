package com.ride_hailing.conductores.domain.repository;

import java.util.Optional;

import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;

public interface ConductorRepository {

    Conductor save(Conductor conductorToSave);
    Optional<Conductor> findById(DriverId driverIdStr);
    Optional<Conductor> findByDocumentoIdentidad(DocumentoIdentidad documentoIdentidad);
    boolean existsByDocumento(DocumentoIdentidad documento);
    

}
