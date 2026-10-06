package com.ride_hailing.viajes;

import java.util.List;
import java.util.UUID;

public interface ViajeUseCase {

    Viaje solicitarViaje(UUID pasajeroId, String origen, String destino);

    void asignarConductor(UUID viajeId, UUID conductorId);

    void iniciarViaje(UUID viajeId);

    void finalizarViaje(UUID viajeId);

    TransicionEstadoViajeService.ResultadoCancelacion cancelarViaje(UUID viajeId, String motivo);

    Viaje consultarViaje(UUID viajeId);

    List<Viaje> listarViajesDelPasajero(UUID pasajeroId);
}
