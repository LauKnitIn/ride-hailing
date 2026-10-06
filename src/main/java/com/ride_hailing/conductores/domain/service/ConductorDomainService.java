package com.ride_hailing.conductores.domain.service;

import com.ride_hailing.conductores.domain.excepcion.ConductorConViajeActivoException;
import com.ride_hailing.conductores.domain.model.Conductor;

public class ConductorDomainService {
    public void validarDesactivacion (Conductor conductor, boolean tieneViajeActivo) {
        if (tieneViajeActivo) {
            throw new ConductorConViajeActivoException(conductor.getIdConductor());
        }
    }
}
