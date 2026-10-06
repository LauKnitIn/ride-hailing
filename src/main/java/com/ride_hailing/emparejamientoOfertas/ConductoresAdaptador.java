package com.ride_hailing.emparejamientoOfertas;

import java.util.List;

import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;

public class ConductoresAdaptador implements ConductoresPort {
    // Inyecta aquí la dependencia real que gestiona los conductores (Servicio o Repositorio de Conductores)
    // private final ConductorService conductorService;

    // public ConductoresAdapter(ConductorService conductorService) {
    //     this.conductorService = conductorService;
    // }

    @Override
    public List<CandidatoConductor> obtenerConductoresDisponibles(double latitud, double longitud, double radioKm) {
        // 1. Consultar conductores disponibles desde el módulo Conductores
        // 2. Mapear la respuesta a objetos CandidatoConductor
        
        return List.of();
    }
}
