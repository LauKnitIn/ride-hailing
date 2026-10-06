package com.ride_hailing.pasajeros;

import java.util.UUID;

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
