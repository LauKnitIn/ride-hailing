package com.ride_hailing.emparejamientoOfertas.application.puerto.entrada;

import java.util.List;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

public interface ConsultarOfertasUseCase {
    Oferta porId(String ofertaId);
    List<Oferta> porViaje(String viajeId);
}
