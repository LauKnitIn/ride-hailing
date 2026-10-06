package com.ride_hailing.pasajeros.aplicacion;

import java.util.UUID;

import com.ride_hailing.pasajeros.dominio.Pasajero;

public interface PasajeroUseCase {

    Pasajero registrar(String nombre, String correo, String telefono);

    void actualizarPerfil(UUID pasajeroId, String nuevoNombre, String nuevoTelefono);

    void cambiarCorreo(UUID pasajeroId, String nuevoCorreo);

    Pasajero consultar(UUID pasajeroId);
}
