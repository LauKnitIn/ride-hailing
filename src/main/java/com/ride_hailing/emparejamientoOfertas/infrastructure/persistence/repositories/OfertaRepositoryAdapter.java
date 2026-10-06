package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;
import com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities.OfertaEntity;

@Repository
public class OfertaRepositoryAdapter implements OfertaRepository {
    
    private final SpringDataOfertaRepository repository;

    // Constructor de la clase
    public OfertaRepositoryAdapter(SpringDataOfertaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Oferta guardar(Oferta oferta) {
        OfertaEntity entity = new OfertaEntity(
                oferta.getId().value(),
                oferta.getViajeId(),
                oferta.getConductorId(),
                oferta.getEstado().name(),
                oferta.getFechaCreacion()
        );
        repository.save(entity);
        return oferta;
    }

    @Override
    public Optional<Oferta> buscarPorId(OfertaId id) {
        return repository.findById(id.value())
                .map(entity -> OfertaFactory.reconstruir(
                        entity.getId(),
                        entity.getViajeId(),
                        entity.getConductorId(),
                        EstadoOferta.valueOf(entity.getEstado()),
                        entity.getFechaCreacion()
                ));
    }

    @Override
    public List<Oferta> buscarPorViajeId(UUID viajeId) {
        return repository.findByViajeId(viajeId).stream()
                .map(entity -> OfertaFactory.reconstruir(
                        entity.getId(),
                        entity.getViajeId(),
                        entity.getConductorId(),
                        EstadoOferta.valueOf(entity.getEstado()),
                        entity.getFechaCreacion()
                ))
                .toList();
    }
}

