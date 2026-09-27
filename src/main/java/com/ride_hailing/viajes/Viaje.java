package com.ride_hailing.viajes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Paso 4 · Límite del Agregado.
 *
 * Raíz del Agregado del subdominio "Viajes y seguimiento" (Dev 2).
 *
 * Regla de límite: {@code Viaje} referencia a Pasajero y Conductor SOLO por su
 * id (nunca por el objeto completo), porque esas entidades pertenecen a otros
 * subdominios (Pasajero, y Conductor del subdominio "Disponibilidad de
 * conductores" de Dev 1) — igual que {@code Publicacion.investigadorCorreo} en
 * el ejemplo de RICA del taller.
 *
 * La construcción está delegada a {@link ViajeFactory}: este constructor es
 * de paquete (no público) para que nadie cree un Viaje saltándose las
 * validaciones de la Factory.
 */
public class Viaje {

    private final UUID id;
    private final UUID pasajeroId;
    private UUID conductorId; // null hasta que ConductorAsignado ocurre
    private final String origen;
    private final String destino;
    private EstadoViaje estado;
    private final LocalDateTime horaSolicitud;
    private LocalDateTime horaAsignacion;
    private LocalDateTime horaFinalizacion;
    private String motivoCancelacion;

    Viaje(UUID id, UUID pasajeroId, String origen, String destino, LocalDateTime horaSolicitud) {
        this.id = id;
        this.pasajeroId = pasajeroId;
        this.origen = origen;
        this.destino = destino;
        this.horaSolicitud = horaSolicitud;
        this.estado = EstadoViaje.solicitado();
    }

    /** HU-03 / HU-04: se asigna un conductor (por id) al viaje. */
    public void asignarConductor(UUID conductorId, LocalDateTime momento) {
        Objects.requireNonNull(conductorId, "conductorId no puede ser nulo");
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.ASIGNADO);
        this.conductorId = conductorId;
        this.horaAsignacion = momento;
    }

    /** El conductor recoge al pasajero y el viaje pasa a "en curso". */
    public void iniciar() {
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.EN_CURSO);
    }

    /** HU-06 / HU-07: el viaje termina y queda listo para el cálculo de tarifa. */
    public void finalizar(LocalDateTime momento) {
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.FINALIZADO);
        this.horaFinalizacion = momento;
    }

    /** HU-10: el pasajero o el conductor cancelan, con motivo obligatorio. */
    public void cancelar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo de cancelación es obligatorio (HU-10)");
        }
        this.estado = this.estado.transicionarA(EstadoViaje.Valor.CANCELADO);
        this.motivoCancelacion = motivo;
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

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public EstadoViaje getEstado() {
        return estado;
    }

    public LocalDateTime getHoraSolicitud() {
        return horaSolicitud;
    }

    public LocalDateTime getHoraAsignacion() {
        return horaAsignacion;
    }

    public LocalDateTime getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }
}
