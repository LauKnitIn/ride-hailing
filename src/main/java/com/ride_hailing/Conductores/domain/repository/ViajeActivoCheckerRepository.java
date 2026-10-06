package com.ride_hailing.Conductores.domain;

import com.ride_hailing.Conductores.domain.model.DriverId;

public interface ViajeActivoCheckerRepository {
    boolean tieneViajeEnCurso(DriverId driverId);
}
