package com.ride_hailing.tarifas.infraestructure.salida;

import java.util.UUID;

import com.ride_hailing.tarifas.application.DatosViajeTarifa;
import com.ride_hailing.tarifas.application.RepositorioViajes;

public class ViajeAdapter implements RepositorioViajes {

    @Override
    public DatosViajeTarifa buscarPorId(UUID viajeId) {

        // Simulación temporal del acceso al módulo Viajes

        return new DatosViajeTarifa(
                10.0, // distancia km
                15.0  // tiempo minutos
        );
    }
}