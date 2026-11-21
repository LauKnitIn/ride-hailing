package com.ride_hailing.viajes.infraestructura.salida.conductores;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.UbicacionConductorPort;
import com.ride_hailing.viajes.dominio.Ubicacion;

@Component
public class UbicacionConductorPendiente implements UbicacionConductorPort {

    @Override
    public Optional<Ubicacion> ubicacionActual(UUID conductorId) {
        return Optional.empty();
    }
}