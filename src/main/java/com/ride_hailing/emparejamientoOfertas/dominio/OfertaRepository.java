package com.ride_hailing.emparejamientoOfertas.dominio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfertaRepository {
    Oferta guardar(Oferta oferta);
    Optional<Oferta> buscarPorId(OfertaId id);
    List<Oferta> buscarPorViajeId(UUID viajeId);
    List<Oferta> buscarPorEstado(EstadoOferta estado);
}