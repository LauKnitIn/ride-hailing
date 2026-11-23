package com.ride_hailing.calificacion.infraestructure.salida;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.RepositorioViajes;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;
import com.ride_hailing.viajes.dominio.EstadoViaje;
import com.ride_hailing.viajes.dominio.Viaje;

/** Adaptador hacia el puerto de entrada del subdominio Viajes. */
@Component("calificacionViajeAdapter")
public class ViajeAdapter implements RepositorioViajes {

    private final ViajeUseCase viajeUseCase;

    public ViajeAdapter(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @Override
    public DatosViajeCalificacion buscarPorId(UUID viajeId) {
        Viaje viaje = viajeUseCase.consultarViaje(viajeId);
        return new DatosViajeCalificacion(
                viaje.getPasajeroId(),
                viaje.getConductorId(),
                viaje.getEstado().valor() == EstadoViaje.Valor.FINALIZADO);
    }
}
