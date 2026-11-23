package com.ride_hailing.tarifas.application;

import java.util.UUID;

public interface RepositorioViajes {

    DatosViajeTarifa buscarPorId(UUID viajeId);
}