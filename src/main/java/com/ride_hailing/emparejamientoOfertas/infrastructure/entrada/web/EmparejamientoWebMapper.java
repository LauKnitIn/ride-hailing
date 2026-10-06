package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

@Component
public class EmparejamientoWebMapper {
    public OfertaResponse toResponse(Oferta oferta) {
        return new OfertaResponse(
                oferta.getId().value(),
                oferta.getViajeId().value(),
                oferta.getConductorId(),
                oferta.getEstado().name()
        );
    }
}
