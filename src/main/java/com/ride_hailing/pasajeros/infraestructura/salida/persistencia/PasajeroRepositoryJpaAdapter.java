package com.ride_hailing.pasajeros.infraestructura.salida.persistencia;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.ride_hailing.pasajeros.aplicacion.RepositorioPasajeros;
import com.ride_hailing.pasajeros.dominio.Pasajero;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class PasajeroRepositoryJpaAdapter implements RepositorioPasajeros {

    private final PasajeroRepository pasajeroRepository;

    public PasajeroRepositoryJpaAdapter(PasajeroRepository pasajeroRepository) {
        this.pasajeroRepository = pasajeroRepository;
    }

    @Override
    public void guardar(Pasajero pasajero) {
        pasajeroRepository.save(pasajero);
    }

    @Override
    public Optional<Pasajero> buscarPorId(UUID pasajeroId) {
        return pasajeroRepository.findById(pasajeroId);
    }

    @Override
    public List<Pasajero> listarTodos() {
        return pasajeroRepository.findAll();
    }
}
