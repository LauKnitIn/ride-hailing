package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "conductores")
public class ConductorEntity {

    @Id
    private UUID id;
    private Boolean disponible;
    private Double latitud;
    private Double longitud;

    public ConductorEntity() {
    }

    public ConductorEntity(UUID id, Boolean disponible, Double latitud, Double longitud) {
        this.id = id;
        this.disponible = disponible;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}