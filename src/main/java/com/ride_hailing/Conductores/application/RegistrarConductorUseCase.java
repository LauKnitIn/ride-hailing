package com.ride_hailing.Conductores.application;

import org.springframework.stereotype.Service;

import com.ride_hailing.Conductores.domain.factory.ConductorFactory;
import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.repository.ConductorRepository;
import com.ride_hailing.Conductores.infrastructure.web.dto.RegistrarConductorRequest;

import jakarta.transaction.Transactional;

@Service
public class RegistrarConductorUseCase {
    private final ConductorRepository conductorRepository;

    public RegistrarConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Transactional
    public Conductor ejecutar(RegistrarConductorRequest request) {
        Conductor nuevoConductor = ConductorFactory.crearConductor(
            request.tipoDocumento(),
            request.numeroDocumento(),
            request.nombreCompleto(),
            request.fechaNacimiento()
        );
        return conductorRepository.save(nuevoConductor);
    }
}
