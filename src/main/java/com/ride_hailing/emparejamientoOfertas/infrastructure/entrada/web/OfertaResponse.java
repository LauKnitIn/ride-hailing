package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

public record OfertaResponse(String id, String viajeId, String conductorId, String estado) {

    public static OfertaResponse desde(Oferta oferta) {
        return new OfertaResponse(
                oferta.getId().value(),
                oferta.getViajeId().value(),
                oferta.getConductorId(),
                oferta.getEstado().name());
    }
}
