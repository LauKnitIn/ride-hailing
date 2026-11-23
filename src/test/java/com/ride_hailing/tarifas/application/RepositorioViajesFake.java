package com.ride_hailing.tarifas.application;

import java.util.UUID;

public class RepositorioViajesFake implements RepositorioViajes {

    @Override
    public DatosViajeTarifa buscarPorId(UUID viajeId) {
        return new DatosViajeTarifa(
                10.0,
                20.0);
    }

}
