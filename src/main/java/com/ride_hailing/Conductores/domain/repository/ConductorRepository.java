package com.ride_hailing.Conductores.domain;

import java.util.Optional;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.model.DriverId;

public interface ConductorRepository {

    Conductor save(Conductor conductorToSave);
    Optional<Conductor> findById(DriverId driverIdStr);
    Optional<Conductor> findByDocumentoIdentidad(DocumentoIdentidad documentoIdentidad);
    boolean existsByDocumento(DocumentoIdentidad documento);
    

}
