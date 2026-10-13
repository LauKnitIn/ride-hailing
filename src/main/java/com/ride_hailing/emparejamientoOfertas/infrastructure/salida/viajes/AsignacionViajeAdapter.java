package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

import java.util.UUID;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;

@Component
public class AsignacionViajeAdapter implements AsignacionViajePort {

     private final ViajeUseCase viajeUseCase;

    public AsignacionViajeAdapter(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @Override
    public void asignar(String viajeId, String conductorId) {
        viajeUseCase.asignarConductor(UUID.fromString(viajeId), UUID.fromString(conductorId));
    }

    @Override
    public void rechazarPorFaltaDeConductores(String viajeId, String motivo) {
        viajeUseCase.rechazarPorFaltaDeConductores(UUID.fromString(viajeId), motivo);
    }
}
