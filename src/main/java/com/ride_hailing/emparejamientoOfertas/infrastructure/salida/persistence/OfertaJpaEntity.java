package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "ofertas", indexes = @Index(name = "idx_ofertas_viaje", columnList = "viajeId"))
class OfertaJpaEntity {

    @Id
    String id;
    String viajeId;
    String conductorId;
    @Enumerated(EnumType.STRING)
    EstadoOferta estado;

    protected OfertaJpaEntity() {
    }
}
