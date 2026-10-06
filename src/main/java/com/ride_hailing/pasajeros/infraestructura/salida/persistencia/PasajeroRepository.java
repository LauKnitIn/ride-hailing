package com.ride_hailing.pasajeros.infraestructura.salida.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ride_hailing.pasajeros.dominio.Pasajero;

import java.util.UUID;

public interface PasajeroRepository extends JpaRepository<Pasajero, UUID> {
}
