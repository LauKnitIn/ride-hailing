package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.evento;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.viajes.aplicacion.ViajeSolicitadoEvent;

/** Al solicitarse un viaje, inicia el emparejamiento con el conductor más cercano (HU-03). */
@Component
public class ViajeSolicitadoListener {

    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitud;

    public ViajeSolicitadoListener(ProcesarSolicitudEmparejamientoUseCase procesarSolicitud) {
        this.procesarSolicitud = procesarSolicitud;
    }

    @EventListener
    public void alSolicitarViaje(ViajeSolicitadoEvent evento) {
        procesarSolicitud.procesar(
                evento.viajeId().toString(), evento.latitudOrigen(), evento.longitudOrigen());
    }
}
