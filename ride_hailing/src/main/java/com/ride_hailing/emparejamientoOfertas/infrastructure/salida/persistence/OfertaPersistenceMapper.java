package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

@Component
public class OfertaPersistenceMapper {

    public OfertaJpaEntity toJpaEntity(Oferta oferta) {
        return new OfertaJpaEntity(
                oferta.getId().value(),
                oferta.getViajeId().value(),
                oferta.getConductorId(),
                oferta.getEstado()
        );
    }

    public Oferta toDomain(OfertaJpaEntity entity) {
        return Oferta.reconstruir(
                new OfertaId(entity.getId()),
                new ViajeId(entity.getViajeId()),
                entity.getConductorId(),
                entity.getEstado()
        );
    }
}
