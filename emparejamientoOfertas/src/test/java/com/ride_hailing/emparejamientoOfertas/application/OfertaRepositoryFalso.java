package com.ride_hailing.emparejamientoOfertas.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

public class OfertaRepositoryFalso implements OfertaRepositoryPort {

    private final Map<String, Oferta> almacen = new HashMap<>();

    @Override
    public Oferta guardar(Oferta oferta) {
        almacen.put(oferta.getId().value(), oferta);
        return oferta;
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return Optional.ofNullable(almacen.get(id.value()));
    }

    @Override
    public List<Oferta> buscarPorViajeId(ViajeId viajeId) {
        return almacen.values().stream()
                .filter(o -> o.getViajeId().equals(viajeId))
                .toList();
    }

    @Override
    public boolean existePorViajeId(ViajeId viajeId) {
        return almacen.values().stream()
                .anyMatch(o -> o.getViajeId().equals(viajeId));
    }

    @Override
    public List<Oferta> buscarPorConductorIdyEstado(String conductorId, EstadoOferta estado) {
        return almacen.values().stream()
                .filter(o -> o.getConductorId().equals(conductorId) && o.getEstado() == estado)
                .toList();
    }

    
}
