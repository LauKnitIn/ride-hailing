package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

@Component
public class OfertaRepositoryAdapter implements OfertaRepositoryPort {

    private final OfertaJpaRepository jpaRepository;
    private final OfertaPersistenceMapper mapper;

    public OfertaRepositoryAdapter(OfertaJpaRepository jpaRepository, OfertaPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Oferta guardar(Oferta oferta) {
        OfertaJpaEntity saved = jpaRepository.save(mapper.toJpaEntity(oferta));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Oferta> buscarPorViajeId(ViajeId viajeId) {
        return jpaRepository.findByViajeId(viajeId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorViajeId(ViajeId viajeId) {
        return jpaRepository.existsByViajeId(viajeId.value());
    }

    @Override
    public List<Oferta> buscarPorConductorIdyEstado(String conductorId, EstadoOferta estado) {
        return jpaRepository.findByConductorIdAndEstado(conductorId, estado).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
