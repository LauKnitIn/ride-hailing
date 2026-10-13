package com.ride_hailing.emparejamientoOfertas.application.puerto.salida;

public interface AsignacionViajePort {
    void asignar(String viajeId, String conductorId);

    void rechazarPorFaltaDeConductores(String viajeId, String motivo);
}
