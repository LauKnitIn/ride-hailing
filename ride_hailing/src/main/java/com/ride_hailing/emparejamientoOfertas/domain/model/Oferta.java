package com.ride_hailing.emparejamientoOfertas.domain.model;

import java.util.Objects;

import com.ride_hailing.emparejamientoOfertas.domain.exception.EstadoOfertaInvalidoException;

public class Oferta {
    private final OfertaId id;
    private final ViajeId viajeId;
    private final String conductorId;
    private EstadoOferta estado;

    public Oferta(OfertaId id, ViajeId viajeId, String conductorId, EstadoOferta estado) {
        this.id = Objects.requireNonNull(id, "id es requerido");
        this.viajeId = Objects.requireNonNull(viajeId, "viajeId es requerido");
        this.conductorId = Objects.requireNonNull(conductorId, "conductorId es requerido");
        if (conductorId.trim().isEmpty()) {
            throw new IllegalArgumentException("conductorId no puede estar vacío");
        }
        this.estado = Objects.requireNonNull(estado, "estado es requerido");
    }


    public static Oferta reconstruir(OfertaId id, ViajeId viajeId, String conductorId, EstadoOferta estado) {
        return new Oferta(id, viajeId, conductorId, estado);
    }

    public void aceptar() {
        if (this.estado != EstadoOferta.PROPUESTA) {
            throw new EstadoOfertaInvalidoException("No se puede aceptar una oferta en estado: " + this.estado);
        }
        this.estado = EstadoOferta.ACEPTADA;
    }

    public void meRechazar() {
        if (this.estado != EstadoOferta.PROPUESTA) {
            throw new EstadoOfertaInvalidoException("No se puede rechazar una oferta en estado: " + this.estado);
        }
        this.estado = EstadoOferta.RECHAZADA;
    }

    public OfertaId getId() {
        return id;
    }

    public ViajeId getViajeId() {
        return viajeId;
    }

    public String getConductorId() {
        return conductorId;
    }

    public EstadoOferta getEstado() {
        return estado;
    }

}
