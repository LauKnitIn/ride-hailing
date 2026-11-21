package com.ride_hailing.viajes.aplicacion;

import java.util.Optional;
import java.util.UUID;

import com.ride_hailing.viajes.dominio.Ubicacion;

/** Puerto de salida hacia el subdominio Conductores para conocer su ubicacion actual. */
public interface UbicacionConductorPort {

    Optional<Ubicacion> ubicacionActual(UUID conductorId);
}
