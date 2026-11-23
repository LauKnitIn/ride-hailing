package com.ride_hailing.viajes.infraestructura.salida.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.viajes.dominio.Viaje;

import java.util.List;
import java.util.UUID;

public interface ViajeRepository extends JpaRepository<Viaje, UUID> {

    List<Viaje> findByConductorId(UUID conductorId);

    List<Viaje> findByPasajeroId(UUID pasajeroId);
}
