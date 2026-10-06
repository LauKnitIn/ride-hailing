package com.ride_hailing.emparejamientoOfertas;

import java.time.Instant;

public class ResponderOfertaUseCase {
    private final OfertaRepository ofertaRepository;
    private final AsignacionViajePort asignacionViajePort;
    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase;

    public ResponderOfertaUseCase(
            OfertaRepository ofertaRepository,
            AsignacionViajePort asignacionViajePort,
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase) {
        this.ofertaRepository = ofertaRepository;
        this.asignacionViajePort = asignacionViajePort;
        this.procesarSolicitudEmparejamientoUseCase = procesarSolicitudEmparejamientoUseCase;
    }

    public void responder(OfertaId ofertaId, boolean acepta, double origenLat, double origenLon) {
        Oferta oferta = ofertaRepository.buscarPorId(ofertaId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

        if (acepta) {
            oferta.aceptar();
            ofertaRepository.guardar(oferta);
            asignacionViajePort.notificarConductorAsignado(oferta.getViajeId(), oferta.getConductorId(), Instant.now());
        } else {
            oferta.rechazar();
            ofertaRepository.guardar(oferta);
            procesarSolicitudEmparejamientoUseCase.ejecutar(oferta.getViajeId(), origenLat, origenLon);
        }
    }
}
