package com.ride_hailing.emparejamientoOfertas.dominio;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

public class EmparejamientoDomainService {

    public record CandidatoConductor(UUID conductorId, double latitud, double longitud) {}

    public CandidatoConductor seleccionarSiguienteCandidato(
            double origenLat,
            double origenLon,
            List<CandidatoConductor> conductoresDisponibles,
            List<UUID> conductoresQueRechazaron,
            List<Oferta> ofertasPendientes,
            double radioMaximoKm) {

        // Obtener los IDs de conductores con ofertas en estado PENDIENTE
        Set<UUID> conductoresConOfertaPendiente = ofertasPendientes.stream()
                .map(Oferta::getConductorId)
                .collect(Collectors.toSet());

        return conductoresDisponibles.stream()
                // Excluir a los conductores que ya rechazaron la solicitud
                .filter(c -> !conductoresQueRechazaron.contains(c.conductorId()))
                // Excluir a los conductores que actualmente tienen una oferta PENDIENTE
                .filter(c -> !conductoresConOfertaPendiente.contains(c.conductorId()))
                // Filtrar solo los conductores dentro del radio máximo permitido
                .filter(c -> calcularDistanciaKm(origenLat, origenLon, c.latitud(), c.longitud()) <= radioMaximoKm)
                // Ordenar por cercanía (de menor a mayor distancia al punto de origen)
                .min(Comparator.comparingDouble(
                        c -> calcularDistanciaKm(origenLat, origenLon, c.latitud(), c.longitud())
                ))
                .orElse(null);
    }

    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
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