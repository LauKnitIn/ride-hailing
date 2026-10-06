package com.ride_hailing.Conductores.domain;

public interface ViajeActivoCheckerRepository {
    boolean tieneViajeEnCurso(DriverId driverId);
}
