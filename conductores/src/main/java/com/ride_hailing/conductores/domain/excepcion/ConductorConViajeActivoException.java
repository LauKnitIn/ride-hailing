package com.ride_hailing.conductores.domain.excepcion;

import com.ride_hailing.conductores.domain.model.DriverId;

public class ConductorConViajeActivoException extends RuntimeException {
    public ConductorConViajeActivoException(DriverId id) {
        super("El conductor " + id.value() + " tiene un viaje activo y no puede salir de servicio");
    }
}
