package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;
import com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.repositories.SpringDataOfertaRepository;

@Repository
public class OfertaRepositoryAdapter implements OfertaRepository{
    private final SpringDataOfertaRepository repository;

    public OfertaRepositoryAdapter(SpringDataOfertaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Oferta guardar(Oferta oferta) {
        OfertaEntity entity = new OfertaEntity(
                oferta.getId().value(),
                oferta.getViajeId(),
                oferta.getConductorId(),
                oferta.getOrigenLat(),
                oferta.getOrigenLon(),
                oferta.getEstado().name(),
                oferta.getFechaCreacion()
        );
        repository.save(entity);
        return oferta;
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return repository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<Oferta> buscarPorViajeId(UUID viajeId) {
        return repository.findByViajeId(viajeId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Oferta> buscarPorEstado(EstadoOferta estado) {
        return repository.findByEstado(estado.name()).stream().map(this::toDomain).toList();
    }

    private Oferta toDomain(OfertaEntity entity) {
        return OfertaFactory.reconstruir(
                entity.getId(),
                entity.getViajeId(),
                entity.getConductorId(),
                entity.getOrigenLat(),
                entity.getOrigenLon(),
                EstadoOferta.valueOf(entity.getEstado()),
                entity.getFechaCreacion()
        );
    }

}
