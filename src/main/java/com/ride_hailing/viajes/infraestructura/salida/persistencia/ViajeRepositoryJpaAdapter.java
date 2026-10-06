package com.ride_hailing.viajes.infraestructura.salida.persistencia;

import org.springframework.stereotype.Repository;

import com.ride_hailing.viajes.aplicacion.RepositorioViajes;
import com.ride_hailing.viajes.dominio.Viaje;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ViajeRepositoryJpaAdapter implements RepositorioViajes {

    private final ViajeRepository viajeRepository;

    public ViajeRepositoryJpaAdapter(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @Override
    public void guardar(Viaje viaje) {
        viajeRepository.save(viaje);
    }

    @Override
    public Optional<Viaje> buscarPorId(UUID viajeId) {
        return viajeRepository.findById(viajeId);
    }

    @Override
    public List<Viaje> buscarPorConductorId(UUID conductorId) {
        return viajeRepository.findByConductorId(conductorId);
    }

    @Override
    public List<Viaje> buscarPorPasajeroId(UUID pasajeroId) {
        return viajeRepository.findByPasajeroId(pasajeroId);
    }
}
