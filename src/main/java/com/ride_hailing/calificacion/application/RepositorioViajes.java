package com.ride_hailing.calificacion.application;

import java.util.UUID;

public interface RepositorioViajes {

        DatosViajeCalificacion buscarPorId(UUID viajeId);

}
