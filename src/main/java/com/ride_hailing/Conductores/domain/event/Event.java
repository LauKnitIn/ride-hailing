package com.ride_hailing.Conductores.domain.event;

import java.time.Instant;

import com.ride_hailing.Conductores.domain.model.DriverId;
import com.ride_hailing.Conductores.domain.model.UbicacionGeografica;

public class Event {

    public record DisponibilidadRegistradaEvent(
        DriverId driverId,
        UbicacionGeografica ubicacion,
        Instant fechaOcurrencia) {
        public DisponibilidadRegistradaEvent(DriverId driverId, UbicacionGeografica ubicacion) {
            this(driverId, ubicacion, Instant.now());
        }
    }


    public record DisponibilidadDesactivadaEvent(
        DriverId driverId,
        Instant fechaOcurrencia
    ) {
        public DisponibilidadDesactivadaEvent(DriverId driverId) {
            this(driverId, Instant.now());
        }
    }

    public record UbicacionConductorActualizadaEvent(
        DriverId driverId,
        UbicacionGeografica nuevaUbicacion,
        Instant fechaOcurrencia
    ) {
        public UbicacionConductorActualizadaEvent(DriverId driverId, UbicacionGeografica nuevaUbicacion) {
            this(driverId, nuevaUbicacion, Instant.now());
        }
    }

}
