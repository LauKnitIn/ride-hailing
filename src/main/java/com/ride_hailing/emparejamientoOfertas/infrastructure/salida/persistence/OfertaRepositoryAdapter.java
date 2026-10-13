package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

@Component
class OfertaRepositoryAdapter implements OfertaRepositoryPort {

    private final OfertaJpaRepository jpaRepository;

    OfertaRepositoryAdapter(OfertaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Oferta guardar(Oferta oferta) {
        return aDominio(jpaRepository.save(aEntidad(oferta)));
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return jpaRepository.findById(id.value()).map(this::aDominio);
    }

    @Override
    public List<Oferta> buscarPorViajeId(ViajeId viajeId) {
        return jpaRepository.findByViajeId(viajeId.value()).stream().map(this::aDominio).toList();
    }

    private OfertaJpaEntity aEntidad(Oferta oferta) {
        OfertaJpaEntity e = new OfertaJpaEntity();
        e.id = oferta.getId().value();
        e.viajeId = oferta.getViajeId().value();
        e.conductorId = oferta.getConductorId();
        e.estado = oferta.getEstado();
        return e;
    }

    private Oferta aDominio(OfertaJpaEntity e) {
        return Oferta.reconstruir(new OfertaId(e.id), new ViajeId(e.viajeId), e.conductorId, e.estado);
    }
}
