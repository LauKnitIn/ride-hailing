package com.ride_hailing.viajes.aplicacion;

import java.util.UUID;

public interface VerificadorPasajero {

    boolean existe(UUID pasajeroId);
}