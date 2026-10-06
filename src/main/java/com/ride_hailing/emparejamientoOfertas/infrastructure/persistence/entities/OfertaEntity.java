package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ofertas")
public class OfertaEntity {

    @Id
    private UUID id;
    private UUID viajeId;
    private UUID conductorId;
    private Double origenLat;
    private Double origenLon;
    private String estado;
    private Instant fechaCreacion;

    public OfertaEntity() {
    }

    public OfertaEntity(UUID id, UUID viajeId, UUID conductorId, Double origenLat, Double origenLon, String estado, Instant fechaCreacion) {
        this.id = id;
        this.viajeId = viajeId;
        this.conductorId = conductorId;
        this.origenLat = origenLat;
        this.origenLon = origenLon;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getViajeId() {
        return viajeId;
    }

    public void setViajeId(UUID viajeId) {
        this.viajeId = viajeId;
    }

    public UUID getConductorId() {
        return conductorId;
    }

    public void setConductorId(UUID conductorId) {
        this.conductorId = conductorId;
    }

    public Double getOrigenLat() {
        return origenLat;
    }

    public void setOrigenLat(Double origenLat) {
        this.origenLat = origenLat;
    }

    public Double getOrigenLon() {
        return origenLon;
    }

    public void setOrigenLon(Double origenLon) {
        this.origenLon = origenLon;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}