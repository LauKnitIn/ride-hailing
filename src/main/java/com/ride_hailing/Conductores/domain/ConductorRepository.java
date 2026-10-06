package com.ride_hailing.Conductores.domain;

import java.util.Optional;

public interface ConductorRepository {

    Conductor save(Conductor conductorToSave);
    Optional<Conductor> findById(DriverId driverIdStr);
    Optional<Conductor> findByDocumentoIdentidad(DocumentoIdentidad documentoIdentidad);
    boolean existsByDocumento(DocumentoIdentidad documento);
    

}
