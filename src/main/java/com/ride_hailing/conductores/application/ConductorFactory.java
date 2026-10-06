package com.ride_hailing.conductores.application;

import java.time.LocalDate;

import com.ride_hailing.conductores.application.puerto.salida.ConductorRepositoryPort;
import com.ride_hailing.conductores.domain.excepcion.ConductorDuplicadoException;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;

public class ConductorFactory {
    
    private final ConductorRepositoryPort conductorRepository;

    public ConductorFactory(ConductorRepositoryPort conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Conductor crear(String nombreCompleto, DocumentoIdentidad documento, LocalDate fechaNacimiento) {
    if (conductorRepository.existeDocumento(documento)) {
        throw new ConductorDuplicadoException(documento);
    }
    return new Conductor(DriverId.generar(), documento, nombreCompleto, fechaNacimiento);   // ← antes: Conductor.crear(...)
}
}
