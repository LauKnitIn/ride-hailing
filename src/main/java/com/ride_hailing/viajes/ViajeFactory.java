package com.ride_hailing.viajes;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Paso 5 · Factory.
 *
 * Concentra la validación y construcción de la raíz del Agregado (Viaje),
 * para que sea imposible crear un Viaje en un estado inconsistente
 * (p. ej. sin pasajero, o con origen igual al destino) — igual que
 * {@code InvestigadorFactory} en el taller de RICA.
 */
public class ViajeFactory {

    /** HU-01: un pasajero solicita un viaje indicando origen y destino. */
    public Viaje solicitar(UUID pasajeroId, String origen, String destino) {
        Objects.requireNonNull(pasajeroId, "pasajeroId es obligatorio (HU-01)");
        validarUbicacion(origen, "origen");
        validarUbicacion(destino, "destino");
        if (origen.trim().equalsIgnoreCase(destino.trim())) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser iguales");
        }
        return new Viaje(UUID.randomUUID(), pasajeroId, origen.trim(), destino.trim(), LocalDateTime.now());
    }

    private void validarUbicacion(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio (HU-01)");
        }
    }
}
