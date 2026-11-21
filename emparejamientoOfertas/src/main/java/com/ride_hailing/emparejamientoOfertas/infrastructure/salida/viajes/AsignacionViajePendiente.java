package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;

@Component
public class AsignacionViajePendiente implements AsignacionViajePort {

    private static final Logger log = LoggerFactory.getLogger(AsignacionViajePendiente.class);

    // TODO Etapa 2: notificar a viajes-service por red.
    @Override
    public void asignar(String viajeId, String conductorId) {
        log.info("PENDIENTE: asignar viaje {} al conductor {}", viajeId, conductorId);
    }

    @Override
    public void rechazarPorFaltaDeConductores(String viajeId, String motivo) {
        log.info("PENDIENTE: rechazar viaje {} ({})", viajeId, motivo);
    }
}