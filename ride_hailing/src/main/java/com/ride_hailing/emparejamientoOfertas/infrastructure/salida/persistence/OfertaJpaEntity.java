package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.persistence;

import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ofertas_emparejamiento")
class OfertaJpaEntity {

    @Id
    private String id;

    @Column(name = "viaje_id", nullable = false)
    private String viajeId;

    @Column(name = "conductor_id", nullable = false)
    private String conductorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoOferta estado;

    public OfertaJpaEntity() {
    }

    public OfertaJpaEntity(String id, String viajeId, String conductorId, EstadoOferta estado) {
        this.id = id;
        this.viajeId = viajeId;
        this.conductorId = conductorId;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public EstadoOferta getEstado() {
        return estado;
    }

    public void setEstado(EstadoOferta estado) {
        this.estado = estado;
    }
}
