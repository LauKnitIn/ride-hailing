package com.ride_hailing.emparejamientoOfertas.application.puerto.entrada;

import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

public interface ProcesarSolicitudEmparejamientoUseCase {
    Optional<Oferta> procesar(String viajeId, double origenLatitud, double origenLongitud);
}
