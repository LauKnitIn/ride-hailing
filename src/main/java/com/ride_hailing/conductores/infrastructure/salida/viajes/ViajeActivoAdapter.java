package com.ride_hailing.conductores.infrastructure.salida.viajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.model.DriverId;

@Component
public class ViajeActivoAdapter implements ViajeActivoPort {

    private static final Logger log = LoggerFactory.getLogger(ViajeActivoAdapter.class);

    @Override
    public boolean tieneViajeActivo(DriverId conductorId) {
        log.warn("ViajeActivoAdapter aún no integrado con Viajes; se asume sin viaje activo");
        return false;
    }
}
