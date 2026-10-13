package com.ride_hailing.conductores.infrastructure.salida.viajes;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;

@Component
public class ViajeActivoAdapter implements ViajeActivoPort {

    private final ViajeUseCase viajeUseCase;

    public ViajeActivoAdapter(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @Override
    public boolean tieneViajeActivo(DriverId conductorId) {
        return viajeUseCase.conductorTieneViajeEnCurso(UUID.fromString(conductorId.value()));
    }
}
