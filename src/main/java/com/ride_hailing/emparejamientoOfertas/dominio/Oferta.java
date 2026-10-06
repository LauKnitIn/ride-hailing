package com.ride_hailing.emparejamientoOfertas.dominio;

import java.time.Instant;
import java.util.UUID;

public class Oferta {
    private final OfertaId id;
    private final UUID viajeId;
    private final UUID conductorId;
    private final double origenLat;
    private final double origenLon;
    private EstadoOferta estado;
    private final Instant fechaCreacion;

    // Constructor completo
    public Oferta(OfertaId id, UUID viajeId, UUID conductorId, double origenLat, double origenLon, EstadoOferta estado, Instant fechaCreacion) {
        this.id = id;
        this.viajeId = viajeId;
        this.conductorId = conductorId;
        this.origenLat = origenLat;
        this.origenLon = origenLon;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para nuevas ofertas
    public Oferta(OfertaId id, UUID viajeId, UUID conductorId, double origenLat, double origenLon) {
        this(id, viajeId, conductorId, origenLat, origenLon, EstadoOferta.PENDIENTE, Instant.now());
    }

    public void aceptar() {
        if (this.estado != EstadoOferta.PENDIENTE) {
            throw new IllegalStateException("La oferta no está en estado PENDIENTE");
        }
        this.estado = EstadoOferta.ACEPTADA;
    }

    public void rechazar() {
        if (this.estado != EstadoOferta.PENDIENTE) {
            throw new IllegalStateException("La oferta no está en estado PENDIENTE");
        }
        this.estado = EstadoOferta.RECHAZADA;
    }

    // Getters obligatorios
    public OfertaId getId() { return id; }
    public UUID getViajeId() { return viajeId; }
    public UUID getConductorId() { return conductorId; }
    public double getOrigenLat() { return origenLat; }
    public double getOrigenLon() { return origenLon; }
    public EstadoOferta getEstado() { return estado; }
    public Instant getFechaCreacion() { return fechaCreacion; }
}