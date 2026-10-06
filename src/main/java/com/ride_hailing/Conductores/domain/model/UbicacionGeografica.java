package com.ride_hailing.Conductores.domain.model;

public record UbicacionGeografica(double latitud, double longitud) {
    public UbicacionGeografica{
        if(latitud < - 90 || latitud > 90){
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90 grados.");

        }
        if(longitud < -180 || longitud > 180){
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180 grados.");
        }
    }
}
