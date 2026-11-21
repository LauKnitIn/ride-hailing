package com.ride_hailing.conductores.infrastructure.salida.viajes;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.model.DriverId;

@Component
public class ViajeActivoPendiente implements ViajeActivoPort {

    // TODO Etapa 2 (Comunicación): consultar al servicio de viajes por red.
    // Por ahora este servicio no puede saber si el conductor tiene un viaje activo,
    //  la regla "no desactivar con viaje activo" no se hace cumplir desde aquí.
    @Override
    public boolean tieneViajeActivo(DriverId conductorId) {
        return false;
    }
}