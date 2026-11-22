package com.ride_hailing.emparejamientoOfertas.application.puerto.salida;

import java.util.List;
import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

public interface OfertaRepositoryPort {
    Oferta guardar(Oferta oferta);
    Optional<Oferta> buscarPorId(OfertaId id);
    List<Oferta> buscarPorViajeId(ViajeId viajeId);
}
