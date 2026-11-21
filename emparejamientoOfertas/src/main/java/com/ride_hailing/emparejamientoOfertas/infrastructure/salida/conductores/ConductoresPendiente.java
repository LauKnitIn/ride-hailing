package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.conductores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;

@Component
public class ConductoresPendiente implements ConductoresPort {

    // TODO Etapa 2: cliente hacia conductores-service (GET /api/conductores/disponibles).
    // Por ahora no hay conductores, así que todo emparejamiento termina sin oferta.
    @Override
    public List<ConductorCandidato> obtenerConductoresDisponibles() {
        return List.of();
    }
}