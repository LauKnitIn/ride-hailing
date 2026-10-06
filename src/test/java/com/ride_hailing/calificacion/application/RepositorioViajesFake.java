package com.ride_hailing.calificacion.application;

public class RepositorioViajesFake
        implements RepositorioViajes {

    @Override
    public DatosViajeCalificacion buscarPorId(
            Long viajeId
    ) {

        return new DatosViajeCalificacion(
                100L,
                200L
        );

    }
}