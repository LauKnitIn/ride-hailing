package com.ride_hailing.emparejamientoOfertas.dominio;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Oferta {
    private final OfertaId id;
    private final UUID viajeId;
    private final UUID conductorId;
    private EstadoOferta estado;
    private final Instant fechaCreacion;

    // Constructor para nuevas ofertas
    public Oferta(OfertaId id, UUID viajeId, UUID conductorId) {
        this(id, viajeId, conductorId, EstadoOferta.PENDIENTE, Instant.now());
    }

    // Constructor completo para reconstrucción mediante Factory
    public Oferta(OfertaId id, UUID viajeId, UUID conductorId, EstadoOferta estado, Instant fechaCreacion) {
        this.id = Objects.requireNonNull(id, "El ID de la oferta no puede ser nulo");
        this.viajeId = Objects.requireNonNull(viajeId, "El ID del viaje no puede ser nulo");
        this.conductorId = Objects.requireNonNull(conductorId, "El ID del conductor no puede ser nulo");
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser nulo");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación no puede ser nula");
    }

    public void aceptar() {
        validarEstadoPendiente();
        this.estado = EstadoOferta.ACEPTADA;
    }

    public void rechazar() {
        validarEstadoPendiente();
        this.estado = EstadoOferta.RECHAZADA;
    }

    public void expirar() {
        validarEstadoPendiente();
        this.estado = EstadoOferta.EXPIRADA;
    }

    private void validarEstadoPendiente() {
        if (this.estado != EstadoOferta.PENDIENTE) {
            throw new IllegalStateException("La oferta no está en estado PENDIENTE");
        }
    }

    public OfertaId getId() { return id; }
    public UUID getViajeId() { return viajeId; }
    public UUID getConductorId() { return conductorId; }
    public EstadoOferta getEstado() { return estado; }
    public Instant getFechaCreacion() { return fechaCreacion; }
}
