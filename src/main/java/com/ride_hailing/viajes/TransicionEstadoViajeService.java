package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TransicionEstadoViajeService {

    public void asignarConductor(Viaje viaje, UUID conductorId,
                                  List<Viaje> viajesActivosDelConductor, Instant momento) {
        boolean conductorOcupado = viajesActivosDelConductor.stream()
            .anyMatch(v -> v.getEstado().valor() == EstadoViaje.Valor.EN_CURSO);
        if (conductorOcupado) {
            throw new ConductorNoDisponibleException(
                "El conductor " + conductorId + " ya tiene un viaje en curso");
        }
        viaje.asignarConductor(conductorId, momento);
    }

    public ResultadoCancelacion cancelar(Viaje viaje, String motivo) {
        EstadoViaje.Valor estadoPrevio = viaje.getEstado().valor();
        viaje.cancelar(motivo);

        boolean requiereCargoParcial =
            estadoPrevio == EstadoViaje.Valor.ASIGNADO || estadoPrevio == EstadoViaje.Valor.EN_CURSO;

        return new ResultadoCancelacion(viaje.getId(), requiereCargoParcial);
    }

    public record ResultadoCancelacion(UUID viajeId, boolean requiereCargoParcial) {}
}
