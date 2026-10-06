package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;

@Component
public class AsignacionViajeAdapter implements AsignacionViajePort {

    private static final Logger log = LoggerFactory.getLogger(AsignacionViajeAdapter.class);

    @Override
    public void asignar(String viajeId, String conductorId) {
        // TODO: Conectar al puerto de entrada de Viajes cuando dicho subdominio sea implementado
        log.info("Asignando el viaje {} al conductor {}", viajeId, conductorId);
    }
}
