package com.ride_hailing.conductores.domain.model;

import java.util.Objects;
import java.util.UUID;

public record DriverId(String value) {
    public DriverId{
        Objects.requireNonNull(value, "El ID no puede ser nulo.");
        if(value.isBlank()){
            throw new IllegalArgumentException("El ID del conductor no puede estar vacío");
        }
    }
    public static DriverId generar(){
        return new DriverId(UUID.randomUUID().toString());
    }
}
