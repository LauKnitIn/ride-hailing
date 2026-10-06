package com.ride_hailing.emparejamientoOfertas;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

@Component
public class AsignacionViajeAdapter implements AsignacionViajePort {

    @Override
    public void notificarConductorAsignado(UUID viajeId, UUID conductorId, Instant fechaAsignacion) {
        // Lógica de notificación/evento al asignar conductor
        System.out.println("Viaje " + viajeId + " asignado al conductor " + conductorId + " a las " + fechaAsignacion);
    }

    @Override
    public void notificarSinConductoresDisponibles(UUID viajeId, String motivo) {
        // Lógica de notificación cuando no hay oferta disponible
        System.out.println("Viaje " + viajeId + " sin conductores disponibles. Motivo: " + motivo);
    }
}