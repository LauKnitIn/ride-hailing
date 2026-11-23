package com.ride_hailing.conductores.infrastructure.entrada.evento;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.viajes.aplicacion.ViajeAsignadoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;

/** Mantiene la disponibilidad del conductor sincronizada con el ciclo de vida del viaje. */
@Component
public class ViajeConductorListener {

    private final ConductorUseCase conductorUseCase;

    public ViajeConductorListener(ConductorUseCase conductorUseCase) {
        this.conductorUseCase = conductorUseCase;
    }

    @EventListener
    public void alAsignarViaje(ViajeAsignadoEvent evento) {
        conductorUseCase.marcarEnViaje(new DriverId(evento.conductorId().toString()));
    }

    @EventListener
    public void alFinalizarViaje(ViajeFinalizadoEvent evento) {
        conductorUseCase.liberarDeViaje(new DriverId(evento.conductorId().toString()));
    }

    @EventListener
    public void alCancelarViaje(ViajeCanceladoEvent evento) {
        if (evento.conductorId() != null) {
            conductorUseCase.liberarDeViaje(new DriverId(evento.conductorId().toString()));
        }
    }
}
