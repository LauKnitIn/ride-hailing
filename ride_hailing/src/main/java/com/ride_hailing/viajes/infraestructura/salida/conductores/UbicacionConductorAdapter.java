package com.ride_hailing.viajes.infraestructura.salida.conductores;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.viajes.aplicacion.UbicacionConductorPort;
import com.ride_hailing.viajes.dominio.Ubicacion;

/** Adaptador hacia el puerto de entrada del subdominio Conductores. */
@Component
public class UbicacionConductorAdapter implements UbicacionConductorPort {

    private final ConductorUseCase conductorUseCase;

    public UbicacionConductorAdapter(ConductorUseCase conductorUseCase) {
        this.conductorUseCase = conductorUseCase;
    }

    @Override
    public Optional<Ubicacion> ubicacionActual(UUID conductorId) {
        UbicacionGeografica u = conductorUseCase.buscarPorId(new DriverId(conductorId.toString())).getUbicacionActual();
        return Optional.ofNullable(u).map(g -> new Ubicacion(g.latitud(), g.longitud()));
    }
}
