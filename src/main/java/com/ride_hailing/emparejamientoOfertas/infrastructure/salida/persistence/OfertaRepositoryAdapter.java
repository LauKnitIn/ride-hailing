package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
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
        OfertaJpaEntity entity = mapper.toJpaEntity(oferta);
        OfertaJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Oferta> buscarPorViajeId(ViajeId viajeId) {
        return jpaRepository.findByViajeId(viajeId.value()).stream()
                .map(mapper::toDomain)
                .toList();
    }
   
}
