package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.conductores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;

@Component 
public class ConductoresAdapter implements ConductoresPort{
    private final ConductorUseCase conductorUseCase;

    public ConductoresAdapter(ConductorUseCase conductorUseCase) {
        this.conductorUseCase = conductorUseCase;
    }

    @Override
    public List<ConductorCandidato> obtenerConductoresDisponibles() {
        return conductorUseCase.buscarDisponibles().stream()
                .filter(c -> c.getDisponibilidad() == EstadoDisponibilidad.DISPONIBLE)
                .filter(c -> c.getUbicacionActual() != null)
                .map(c -> new ConductorCandidato(
                        c.getIdConductor().value(),
                        c.getUbicacionActual().latitud(),
                        c.getUbicacionActual().longitud()
                ))
                .toList();
    }
}
