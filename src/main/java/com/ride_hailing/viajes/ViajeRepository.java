package com.ride_hailing.viajes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViajeRepository {

    void guardar(Viaje viaje);

    Optional<Viaje> buscarPorId(UUID viajeId);

    List<Viaje> buscarPorConductorId(UUID conductorId);

    List<Viaje> buscarPorPasajeroId(UUID pasajeroId);
}
