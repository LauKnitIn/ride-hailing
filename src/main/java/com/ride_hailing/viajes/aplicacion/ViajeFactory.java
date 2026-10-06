package com.ride_hailing.viajes.aplicacion;

import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.dominio.Ubicacion;
import com.ride_hailing.viajes.dominio.Viaje;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Component
public class ViajeFactory {

    public Viaje solicitar(UUID pasajeroId, Ubicacion origen, Ubicacion destino) {
        Objects.requireNonNull(pasajeroId, "pasajeroId es obligatorio (HU-01)");
        Objects.requireNonNull(origen, "origen es obligatorio (HU-01)");
        Objects.requireNonNull(destino, "destino es obligatorio (HU-01)");
        if (origen.equals(destino)) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser iguales");
        }
        return new Viaje(UUID.randomUUID(), pasajeroId, origen, destino, Instant.now());
    }
}
