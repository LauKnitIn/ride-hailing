package com.ride_hailing.emparejamientoOfertas;

import java.time.Instant;
import java.util.UUID;

public interface AsignacionViajePort {
    void notificarConductorAsignado(UUID viajeId, UUID conductorId, Instant fechaAsignacion);
    void notificarSinConductoresDisponibles(UUID viajeId, String motivo);
}
