package com.ride_hailing.viajes.aplicacion;

import java.util.UUID;

public interface SeguimientoViajeUseCase {

    /** Ubicacion en vivo del conductor y tiempo estimado de llegada (HU-01, HU-05). */
    SeguimientoViaje seguirConductor(UUID viajeId);

    record SeguimientoViaje(double latitud, double longitud, double distanciaRestanteKm, long etaMinutos) {}
}
