package com.ride_hailing.conductores.domain.repository;

import com.ride_hailing.conductores.domain.model.DriverId;

public interface ViajeActivoCheckerRepository {
    boolean tieneViajeEnCurso(DriverId driverId);
}
