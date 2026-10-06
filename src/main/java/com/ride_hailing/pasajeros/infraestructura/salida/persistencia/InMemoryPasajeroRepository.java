package com.ride_hailing.pasajeros.infraestructura.salida.persistencia;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.ride_hailing.pasajeros.aplicacion.PasajeroRepository;
import com.ride_hailing.pasajeros.dominio.Pasajero;

@Repository
public class InMemoryPasajeroRepository implements PasajeroRepository {

    private final Map<UUID, Pasajero> pasajerosPorId = new ConcurrentHashMap<>();

    @Override
    public void guardar(Pasajero pasajero) {
        pasajerosPorId.put(pasajero.getId(), pasajero);
    }

    @Override
    public Optional<Pasajero> buscarPorId(UUID pasajeroId) {
        return Optional.ofNullable(pasajerosPorId.get(pasajeroId));
    }

    @Override
    public List<Pasajero> listarTodos() {
        return List.copyOf(pasajerosPorId.values());
    }
}
