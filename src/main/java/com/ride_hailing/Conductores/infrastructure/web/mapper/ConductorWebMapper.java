package com.ride_hailing.Conductores.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.infrastructure.web.dto.ConductorResponse;

@Component 
public class ConductorWebMapper {

    public ConductorResponse toResponse(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        Double latitud = conductor.getUbicacionActual() != null ? conductor.getUbicacionActual().latitud() : null;
        Double longitud = conductor.getUbicacionActual() != null ? conductor.getUbicacionActual().longitud() : null;

        return new ConductorResponse(
            conductor.getIdConductor().value(),
            conductor.getDocumentoIdentidad().getTipoDocumento(),
            conductor.getDocumentoIdentidad().getNumeroDocumento(),
            conductor.getNombreCompleto(),
            conductor.getDisponibilidad().name(),
            latitud,
            longitud
        );
    }

}
