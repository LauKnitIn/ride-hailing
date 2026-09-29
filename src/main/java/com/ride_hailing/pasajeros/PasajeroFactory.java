package com.ride_hailing.pasajeros;

import java.util.List;
import java.util.UUID;


public class PasajeroFactory {

    private final RegistroPasajeroService registroPasajeroService;

    public PasajeroFactory(RegistroPasajeroService registroPasajeroService) {
        this.registroPasajeroService = registroPasajeroService;
    }

    public Pasajero registrar(String nombre, String correo, String telefono,
                               List<Pasajero> pasajerosExistentes) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        Correo correoVo = new Correo(correo);
        Telefono telefonoVo = new Telefono(telefono);

        registroPasajeroService.validarCorreoDisponible(correoVo, pasajerosExistentes);

        return new Pasajero(UUID.randomUUID(), nombre.trim(), correoVo, telefonoVo);
    }
}
