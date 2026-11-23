package com.ride_hailing.calificacion.application;

import java.util.UUID;

public class RepositorioViajesFake
        implements RepositorioViajes {

    static final UUID PASAJERO = UUID.randomUUID();
    static final UUID CONDUCTOR = UUID.randomUUID();

    private final boolean finalizado;

    public RepositorioViajesFake() {
        this(true);
    }

    public RepositorioViajesFake(boolean finalizado) {
        this.finalizado = finalizado;
    }

    @Override
    public DatosViajeCalificacion buscarPorId(UUID viajeId) {
        return new DatosViajeCalificacion(PASAJERO, CONDUCTOR, finalizado);
    }
}
