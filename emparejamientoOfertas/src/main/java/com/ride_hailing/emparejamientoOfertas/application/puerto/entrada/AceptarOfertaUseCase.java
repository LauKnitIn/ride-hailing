package com.ride_hailing.emparejamientoOfertas.application.puerto.entrada;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

public interface AceptarOfertaUseCase {
    Oferta aceptar(String ofertaId);
}
