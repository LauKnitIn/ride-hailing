package com.ride_hailing.emparejamientoOfertas.application.puerto.entrada;

import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

public interface RechazarOfertaUseCase {
    Optional<Oferta> rechazar(String ofertaId, double origenLatitud, double origenLongitud);
}
