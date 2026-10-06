package com.ride_hailing.emparejamientoOfertas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfertaRepository {
    Oferta guardar(Oferta oferta);
    Optional<Oferta> buscarPorId(OfertaId id);
    List<Oferta> buscarPorViajeId(UUID viajeId);
}
