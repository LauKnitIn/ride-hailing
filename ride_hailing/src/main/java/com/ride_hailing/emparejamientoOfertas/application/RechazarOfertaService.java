package com.ride_hailing.emparejamientoOfertas.application;

import java.util.Optional;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.exception.OfertaNoEncontradaException;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;

public class RechazarOfertaService implements RechazarOfertaUseCase {
    private final OfertaRepositoryPort ofertaRepositoryPort;
    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase;

    public RechazarOfertaService(
            OfertaRepositoryPort ofertaRepositoryPort,
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase) {
        this.ofertaRepositoryPort = ofertaRepositoryPort;
        this.procesarSolicitudEmparejamientoUseCase = procesarSolicitudEmparejamientoUseCase;
    }

    @Override
    public Optional<Oferta> rechazar(String ofertaIdStr, double origenLatitud, double origenLongitud) {
        OfertaId ofertaId = new OfertaId(ofertaIdStr);
        Oferta oferta = ofertaRepositoryPort.buscarPorId(ofertaId)
                .orElseThrow(() -> new OfertaNoEncontradaException(ofertaIdStr));

        oferta.meRechazar();
        ofertaRepositoryPort.guardar(oferta);

        return procesarSolicitudEmparejamientoUseCase.procesar(
                oferta.getViajeId().value(),
                origenLatitud,
                origenLongitud
        );
    }
}
