package com.ride_hailing.viajes.aplicacion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ride_hailing.viajes.dominio.Viaje;

public interface RepositorioViajes {

    void guardar(Viaje viaje);

    Optional<Viaje> buscarPorId(UUID viajeId);

    List<Viaje> buscarPorConductorId(UUID conductorId);

    List<Viaje> buscarPorPasajeroId(UUID pasajeroId);
}
