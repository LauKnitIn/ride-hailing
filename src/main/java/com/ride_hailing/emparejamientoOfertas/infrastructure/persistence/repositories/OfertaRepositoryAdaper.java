package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;
import com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities.OfertaEntity;

@Repository
public class OfertaRepositoryAdaper implements OfertaRepository {
    
    private final SpringDataOfertaRepository repository;

    public OfertaRepositoryAdaper(SpringDataOfertaRepository repository) {
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
                .map(entity -> {
                    Oferta oferta = new Oferta(
                            new OfertaId(entity.getId()),
                            entity.getViajeId(),
                            entity.getConductorId()
                    );
                    return oferta;
                });
    }


    @Override
    public List<Oferta> buscarPorViajeId(UUID viajeId) {
        return repository.findByViajeId(viajeId).stream()
                .map(entity -> new Oferta(
                        new OfertaId(entity.getId()),
                        entity.getViajeId(),
                        entity.getConductorId()
                ))
                .toList();
    }
}
