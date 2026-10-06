package com.ride_hailing.pasajeros.aplicacion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ride_hailing.pasajeros.dominio.Pasajero;

public interface PasajeroRepository {

    void guardar(Pasajero pasajero);

    Optional<Pasajero> buscarPorId(UUID pasajeroId);

    List<Pasajero> listarTodos();
}
