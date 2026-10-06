package com.ride_hailing.viajes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ViajeService implements ViajeUseCase {

    private final ViajeRepository viajeRepository;
    private final ViajeFactory viajeFactory;
    private final TransicionEstadoViajeService transicionEstadoViajeService;
    private final PublicadorEventosViaje publicadorEventosViaje;

    public ViajeService(ViajeRepository viajeRepository,
                         ViajeFactory viajeFactory,
                         TransicionEstadoViajeService transicionEstadoViajeService,
                         PublicadorEventosViaje publicadorEventosViaje) {
        this.viajeRepository = viajeRepository;
        this.viajeFactory = viajeFactory;
        this.transicionEstadoViajeService = transicionEstadoViajeService;
        this.publicadorEventosViaje = publicadorEventosViaje;
    }

    @Override
    public Viaje solicitarViaje(UUID pasajeroId, double latitudOrigen, double longitudOrigen,
                                 double latitudDestino, double longitudDestino) {
        Ubicacion origen = new Ubicacion(latitudOrigen, longitudOrigen);
        Ubicacion destino = new Ubicacion(latitudDestino, longitudDestino);

        Viaje viaje = viajeFactory.solicitar(pasajeroId, origen, destino);
        viajeRepository.guardar(viaje);

        publicadorEventosViaje.publicar(new ViajeSolicitadoEvent(
            viaje.getId(), pasajeroId, latitudOrigen, longitudOrigen, viaje.getHoraSolicitud()));

        return viaje;
    }

    @Override
    public void asignarConductor(UUID viajeId, UUID conductorId) {
        Viaje viaje = obtenerOFallar(viajeId);
        List<Viaje> viajesActivosDelConductor = viajeRepository.buscarPorConductorId(conductorId);
        Instant momento = Instant.now();

        transicionEstadoViajeService.asignarConductor(viaje, conductorId, viajesActivosDelConductor, momento);
        viajeRepository.guardar(viaje);

        publicadorEventosViaje.publicar(new ViajeAsignadoEvent(viaje.getId(), conductorId, momento));
    }

    @Override
    public void rechazarPorFaltaDeConductores(UUID viajeId, String motivo) {
        Viaje viaje = obtenerOFallar(viajeId);
        transicionEstadoViajeService.cancelar(viaje, motivo);
        viajeRepository.guardar(viaje);

        publicadorEventosViaje.publicar(new SolicitudRechazadaEvent(viaje.getId(), motivo, Instant.now()));
    }

    @Override
    public void iniciarViaje(UUID viajeId) {
        Viaje viaje = obtenerOFallar(viajeId);
        viaje.iniciar(Instant.now());
        viajeRepository.guardar(viaje);
    }

    @Override
    public void finalizarViaje(UUID viajeId) {
        Viaje viaje = obtenerOFallar(viajeId);
        Instant momento = Instant.now();
        viaje.finalizar(momento);
        viajeRepository.guardar(viaje);

        publicadorEventosViaje.publicar(new ViajeFinalizadoEvent(
            viaje.getId(), viaje.getConductorId(), viaje.getDistanciaKm(), viaje.getDuracion(), momento));
    }

    @Override
    public TransicionEstadoViajeService.ResultadoCancelacion cancelarViaje(UUID viajeId, String motivo) {
        Viaje viaje = obtenerOFallar(viajeId);
        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            transicionEstadoViajeService.cancelar(viaje, motivo);
        viajeRepository.guardar(viaje);

        publicadorEventosViaje.publicar(new ViajeCanceladoEvent(
            viaje.getId(), viaje.getConductorId(), resultado.requiereCargoParcial(), Instant.now()));

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