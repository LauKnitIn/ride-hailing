package com.ride_hailing.viajes.dominio;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    private UUID id;
    private UUID pasajeroId;
    private UUID conductorId; // null hasta que ConductorAsignado ocurre
    @Embedded
    @AttributeOverride(name = "latitud", column = @Column(name = "origen_latitud"))
    @AttributeOverride(name = "longitud", column = @Column(name = "origen_longitud"))
    private Ubicacion origen;
    @Embedded
    @AttributeOverride(name = "latitud", column = @Column(name = "destino_latitud"))
    @AttributeOverride(name = "longitud", column = @Column(name = "destino_longitud"))
    private Ubicacion destino;
    @Embedded
    private EstadoViaje estado;
    private Instant horaSolicitud;
    private Instant horaAsignacion;
    private Instant horaInicio;
    private Instant horaFinalizacion;
    private String motivoCancelacion;
    private Double distanciaKm;

    protected Viaje() {
    }

    public Viaje(UUID id, UUID pasajeroId, Ubicacion origen, Ubicacion destino, Instant horaSolicitud) {
        this.id = id;
        this.pasajeroId = pasajeroId;
        this.origen = origen;
        this.destino = destino;
        this.horaSolicitud = horaSolicitud;
        this.estado = EstadoViaje.solicitado();
    }

    public void asignarConductor(UUID conductorId, Instant momento) {
        Objects.requireNonNull(conductorId, "conductorId no puede ser nulo");
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.ASIGNADO);
        this.conductorId = conductorId;
        this.horaAsignacion = momento;
    }

    public void iniciar(Instant momento) {
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.EN_CURSO);
        this.horaInicio = momento;
    }

    public void finalizar(Instant momento) {
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.FINALIZADO);
        this.horaFinalizacion = momento;
        this.distanciaKm = origen.distanciaHaciaKm(destino);
    }

    public void cancelar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo de cancelación es obligatorio (HU-10)");
        }
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.CANCELADO);
        this.motivoCancelacion = motivo;
    }
    
    public Duration getDuracion() {
        if (horaInicio == null || horaFinalizacion == null) {
            return null;
        }
        return Duration.between(horaInicio, horaFinalizacion);
    }

    public UUID getId() {
        return id;
    }

    public UUID getPasajeroId() {
        return pasajeroId;
    }

    public UUID getConductorId() {
        return conductorId;
    }

    public Ubicacion getOrigen() {
        return origen;
    }

    public Ubicacion getDestino() {
        return destino;
    }

    public EstadoViaje getEstado() {
        return estado;
    }

    public Instant getHoraSolicitud() {
        return horaSolicitud;
    }

    public Instant getHoraAsignacion() {
        return horaAsignacion;
    }

    public Instant getHoraInicio() {
        return horaInicio;
    }

    public Instant getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }
}