package com.ride_hailing.tarifas.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifas")
public class TarifaCalculada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID viajeId;
    private double monto;
    private Instant fechaCalculo;

    protected TarifaCalculada() {
    }

    public TarifaCalculada(UUID viajeId, Monto monto) {
        this.viajeId = viajeId;
        this.monto = monto.valor();
        this.fechaCalculo = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public UUID getViajeId() {
        return viajeId;
    }

    public double getMonto() {
        return monto;
    }

    public Instant getFechaCalculo() {
        return fechaCalculo;
    }
}
