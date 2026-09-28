package com.ride_hailing.viajes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryViajeRepository implements ViajeRepository {

    private final Map<UUID, Viaje> viajesPorId = new ConcurrentHashMap<>();

    @Override
    public void guardar(Viaje viaje) {
        viajesPorId.put(viaje.getId(), viaje);
    }

    @Override
    public Optional<Viaje> buscarPorId(UUID viajeId) {
        return Optional.ofNullable(viajesPorId.get(viajeId));
    }

    @Override
    public List<Viaje> buscarPorConductorId(UUID conductorId) {
        return viajesPorId.values().stream()
            .filter(v -> conductorId.equals(v.getConductorId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Viaje> buscarPorPasajeroId(UUID pasajeroId) {
        return viajesPorId.values().stream()
            .filter(v -> pasajeroId.equals(v.getPasajeroId()))
            .collect(Collectors.toList());
    }
}
