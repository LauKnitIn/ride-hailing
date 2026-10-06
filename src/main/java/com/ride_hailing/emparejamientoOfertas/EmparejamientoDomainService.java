package com.ride_hailing.emparejamientoOfertas;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class EmparejamientoDomainService {
    public record CandidatoConductor(UUID conductorId, double latitud, double longitud) {}

    public CandidatoConductor seleccionarSiguienteCandidato(
            double origenLat,
            double origenLon,
            List<CandidatoConductor> conductoresDisponibles,
            List<UUID> conductoresQueRechazaron,
            double radioMaximoKm) {

        return conductoresDisponibles.stream()
                .filter(c -> !conductoresQueRechazaron.contains(c.conductorId()))
                .filter(c -> calcularDistanciaEnKm(origenLat, origenLon, c.latitud(), c.longitud()) <= radioMaximoKm)
                .min(Comparator.comparingDouble(c -> calcularDistanciaEnKm(origenLat, origenLon, c.latitud(), c.longitud())))
                .orElse(null);
    }

    private double calcularDistanciaEnKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
