package com.ride_hailing.emparejamientoOfertas;

import java.time.Instant;
import java.util.Objects;

public class Oferta {
    private final OfertaId id;
    private final UUID viajeId;
    private final UUID conductorId;
    private EstadoOferta estado;
    private final Instant fechaCreacion;

    public Oferta(OfertaId id, UUID viajeId, UUID conductorId) {
        this.id = Objects.requireNonNull(id, "El ID de la oferta no puede ser nulo");
        this.viajeId = Objects.requireNonNull(viajeId, "El ID del viaje no puede ser nulo");
        this.conductorId = Objects.requireNonNull(conductorId, "El ID del conductor no puede ser nulo");
        this.fechaCreacion = Instant.now();
        this.estado = EstadoOferta.PENDIENTE;
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
