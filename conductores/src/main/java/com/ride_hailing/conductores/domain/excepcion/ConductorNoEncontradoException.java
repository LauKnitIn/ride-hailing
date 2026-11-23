package com.ride_hailing.conductores.domain.excepcion;

import com.ride_hailing.conductores.domain.model.DriverId;

public class ConductorNoEncontradoException extends RuntimeException {
    public ConductorNoEncontradoException(DriverId id) {
        super("No existe el conductor " + id.value());
    }
}
