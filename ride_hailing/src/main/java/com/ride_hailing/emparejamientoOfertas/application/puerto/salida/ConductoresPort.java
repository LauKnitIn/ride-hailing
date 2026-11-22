package com.ride_hailing.emparejamientoOfertas.application.puerto.salida;

import java.util.List;

import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;

public interface ConductoresPort {
    List<ConductorCandidato> obtenerConductoresDisponibles();
}
