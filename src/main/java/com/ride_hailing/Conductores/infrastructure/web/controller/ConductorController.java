package com.ride_hailing.Conductores.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.Conductores.application.RegistrarConductorUseCase;
import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.infrastructure.web.dto.ConductorResponse;
import com.ride_hailing.Conductores.infrastructure.web.dto.RegistrarConductorRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conductores")
public class ConductorController {

    private final RegistrarConductorUseCase registrarConductorUseCase;

    public ConductorController(RegistrarConductorUseCase registrarConductorUseCase) {
        this.registrarConductorUseCase = registrarConductorUseCase;
    }

    @PostMapping
    public ResponseEntity<ConductorResponse> registrarConductor(
            @Valid @RequestBody RegistrarConductorRequest request
    ) {
        Conductor conductor = registrarConductorUseCase.ejecutar(request);
        
        Double latitud = conductor.getUbicacionActual() != null ? conductor.getUbicacionActual().latitud() : null;
        Double longitud = conductor.getUbicacionActual() != null ? conductor.getUbicacionActual().longitud() : null;

        ConductorResponse response = new ConductorResponse(
            conductor.getIdConductor().value(),
            conductor.getDocumentoIdentidad().getTipoDocumento(),
            conductor.getDocumentoIdentidad().getNumeroDocumento(),
            conductor.getNombreCompleto(),
            conductor.getDisponibilidad().name(),
            latitud,
            longitud
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
