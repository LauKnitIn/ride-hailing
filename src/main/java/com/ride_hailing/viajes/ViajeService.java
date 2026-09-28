package com.ride_hailing.viajes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ViajeService implements ViajeUseCase {

    private final ViajeRepository viajeRepository;
    private final ViajeFactory viajeFactory;
    private final TransicionEstadoViajeService transicionEstadoViajeService;

    public ViajeService(ViajeRepository viajeRepository,
                         ViajeFactory viajeFactory,
                         TransicionEstadoViajeService transicionEstadoViajeService) {
        this.viajeRepository = viajeRepository;
        this.viajeFactory = viajeFactory;
        this.transicionEstadoViajeService = transicionEstadoViajeService;
    }

    @Override
    public Viaje solicitarViaje(UUID pasajeroId, String origen, String destino) {
        Viaje viaje = viajeFactory.solicitar(pasajeroId, origen, destino);
        viajeRepository.guardar(viaje);
        return viaje;
    }

    @Override
    public void asignarConductor(UUID viajeId, UUID conductorId) {
        Viaje viaje = obtenerOFallar(viajeId);
        List<Viaje> viajesActivosDelConductor = viajeRepository.buscarPorConductorId(conductorId);
        transicionEstadoViajeService.asignarConductor(viaje, conductorId, viajesActivosDelConductor, LocalDateTime.now());
        viajeRepository.guardar(viaje);
    }

    @Override
    public void iniciarViaje(UUID viajeId) {
        Viaje viaje = obtenerOFallar(viajeId);
        viaje.iniciar();
        viajeRepository.guardar(viaje);
    }

    @Override
    public void finalizarViaje(UUID viajeId) {
        Viaje viaje = obtenerOFallar(viajeId);
        viaje.finalizar(LocalDateTime.now());
        viajeRepository.guardar(viaje);
    }

    @Override
    public TransicionEstadoViajeService.ResultadoCancelacion cancelarViaje(UUID viajeId, String motivo) {
        Viaje viaje = obtenerOFallar(viajeId);
        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            transicionEstadoViajeService.cancelar(viaje, motivo);
        viajeRepository.guardar(viaje);
        return resultado;
    }

    @Override
    public Viaje consultarViaje(UUID viajeId) {
        return obtenerOFallar(viajeId);
    }

    @Override
    public List<Viaje> listarViajesDelPasajero(UUID pasajeroId) {
        return viajeRepository.buscarPorPasajeroId(pasajeroId);
    }

    private Viaje obtenerOFallar(UUID viajeId) {
        return viajeRepository.buscarPorId(viajeId)
            .orElseThrow(() -> new ViajeNoEncontradoException(viajeId));
    }
}
