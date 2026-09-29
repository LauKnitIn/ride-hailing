package com.ride_hailing.pasajeros;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasajeroRepository {

    void guardar(Pasajero pasajero);

    Optional<Pasajero> buscarPorId(UUID pasajeroId);

    List<Pasajero> listarTodos();
}
