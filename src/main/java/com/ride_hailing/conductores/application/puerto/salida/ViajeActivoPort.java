package com.ride_hailing.conductores.application.puerto.salida;

import com.ride_hailing.conductores.domain.model.DriverId;

public interface ViajeActivoPort {

    boolean tieneViajeActivo(DriverId conductorId);
}
