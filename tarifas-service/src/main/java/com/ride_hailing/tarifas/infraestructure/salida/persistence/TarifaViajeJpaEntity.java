package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifas_viaje")
class TarifaViajeJpaEntity {

    @Id
    UUID viajeId;
    Double distanciaKm;
    Double tiempoMinutos;
    Double tarifa;
    Double cargoParcial;

    protected TarifaViajeJpaEntity() {
    }

    TarifaViajeJpaEntity(UUID viajeId) {
        this.viajeId = viajeId;
    }
}
