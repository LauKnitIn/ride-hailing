package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifas_viaje")
class TarifaViajeJpaEntity {

    @Id
    @Column(name = "viaje_id", nullable = false)
    UUID viajeId;

    @Column(name = "distancia_km")
    Double distanciaKm;

    @Column(name = "tiempo_minutos")
    Double tiempoMinutos;

    @Column(name = "tarifa")
    Double tarifa;

    @Column(name = "cargo_parcial")
    Double cargoParcial;

    protected TarifaViajeJpaEntity() {
    }

    TarifaViajeJpaEntity(UUID viajeId) {
        this.viajeId = viajeId;
    }
}
