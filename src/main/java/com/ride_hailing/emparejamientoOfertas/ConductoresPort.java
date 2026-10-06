package com.ride_hailing.emparejamientoOfertas;

import java.util.List;

import com.ride_hailing.emparejamientoOfertas.EmparejamientoDomainService.CandidatoConductor;

public interface ConductoresPort {
    List<CandidatoConductor> obtenerConductoresDisponibles(double latitud, double longitud, double radioKm);
}
