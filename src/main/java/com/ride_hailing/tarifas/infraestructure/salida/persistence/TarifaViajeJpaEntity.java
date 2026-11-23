package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Cobros de un viaje: la tarifa al finalizar y/o el cargo parcial si se cancelo tras la asignacion. */
@Entity
@Table(name = "tarifas_viaje")
class TarifaViajeJpaEntity {

    @Id
    UUID viajeId;
    Double tarifa;
    Double cargoParcial;

    protected TarifaViajeJpaEntity() {
    }

    TarifaViajeJpaEntity(UUID viajeId) {
        this.viajeId = viajeId;
    }
}
