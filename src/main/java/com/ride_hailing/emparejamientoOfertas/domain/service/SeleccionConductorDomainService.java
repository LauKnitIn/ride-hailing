package com.ride_hailing.emparejamientoOfertas.domain.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;

public class SeleccionConductorDomainService {
    public Optional<ConductorCandidato> seleccionarMasCercano(
            List<ConductorCandidato> candidatos,
            double origenLatitud,
            double origenLongitud) {

        return candidatos.stream()
                .min(Comparator.comparingDouble(c ->
                        calcularDistanciaHaversine(origenLatitud, origenLongitud, c.latitud(), c.longitud())));
    }

    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
