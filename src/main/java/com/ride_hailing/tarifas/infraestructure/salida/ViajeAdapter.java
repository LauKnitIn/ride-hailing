package com.ride_hailing.tarifas.infraestructure.salida;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.tarifas.application.DatosViajeTarifa;
import com.ride_hailing.tarifas.application.RepositorioViajes;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;
import com.ride_hailing.viajes.dominio.Viaje;

/** Adaptador hacia el puerto de entrada del subdominio Viajes. */
@Component("tarifasViajeAdapter")
public class ViajeAdapter implements RepositorioViajes {

    private final ViajeUseCase viajeUseCase;

    public ViajeAdapter(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @Override
    public DatosViajeTarifa buscarPorId(UUID viajeId) {
        Viaje viaje = viajeUseCase.consultarViaje(viajeId);
        if (viaje.getDistanciaKm() == null || viaje.getDuracion() == null) {
            throw new IllegalStateException("La tarifa solo se calcula para viajes finalizados");
        }
        return new DatosViajeTarifa(viaje.getDistanciaKm(), viaje.getDuracion().toMillis() / 60000.0);
    }
}
