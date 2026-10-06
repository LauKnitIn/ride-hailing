package com.ride_hailing.pasajeros.infraestructura.entrada.web;

import java.util.UUID;

import com.ride_hailing.pasajeros.dominio.Pasajero;

public record PasajeroResponse(UUID id, String nombre, String correo, String telefono) {
    public static PasajeroResponse desde(Pasajero pasajero) {
        return new PasajeroResponse(
            pasajero.getId(),
            pasajero.getNombre(),
            pasajero.getCorreo().valor(),
            pasajero.getTelefono().valor()
        );
    }
}
