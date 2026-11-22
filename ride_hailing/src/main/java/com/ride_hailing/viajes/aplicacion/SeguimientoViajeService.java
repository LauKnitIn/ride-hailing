package com.ride_hailing.viajes.aplicacion;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.viajes.dominio.EstadoViaje;
import com.ride_hailing.viajes.dominio.Ubicacion;
import com.ride_hailing.viajes.dominio.Viaje;

@Service
public class SeguimientoViajeService implements SeguimientoViajeUseCase {

    static final double VELOCIDAD_PROMEDIO_KMH = 30.0;

    private final RepositorioViajes viajeRepository;
    private final UbicacionConductorPort ubicacionConductorPort;

    public SeguimientoViajeService(RepositorioViajes viajeRepository, UbicacionConductorPort ubicacionConductorPort) {
        this.viajeRepository = viajeRepository;
        this.ubicacionConductorPort = ubicacionConductorPort;
    }

    @Override
    public SeguimientoViaje seguirConductor(UUID viajeId) {
        Viaje viaje = viajeRepository.buscarPorId(viajeId)
            .orElseThrow(() -> new ViajeNoEncontradoException(viajeId));

        EstadoViaje.Valor estado = viaje.getEstado().valor();
        if (estado != EstadoViaje.Valor.ASIGNADO && estado != EstadoViaje.Valor.EN_CURSO) {
            throw new IllegalStateException("El seguimiento solo esta disponible con el viaje asignado o en curso");
        }

        Ubicacion conductor = ubicacionConductorPort.ubicacionActual(viaje.getConductorId())
            .orElseThrow(() -> new IllegalStateException("El conductor aun no ha reportado su ubicacion"));

        // Asignado: el conductor se dirige al origen. En curso: se dirige al destino.
        Ubicacion objetivo = estado == EstadoViaje.Valor.ASIGNADO ? viaje.getOrigen() : viaje.getDestino();
        double distanciaKm = conductor.distanciaHaciaKm(objetivo);
        long etaMinutos = (long) Math.ceil(distanciaKm / VELOCIDAD_PROMEDIO_KMH * 60);

        return new SeguimientoViaje(conductor.latitud(), conductor.longitud(), distanciaKm, etaMinutos);
    }
}
