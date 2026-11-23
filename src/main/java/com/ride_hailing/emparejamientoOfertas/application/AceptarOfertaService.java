package com.ride_hailing.emparejamientoOfertas.application;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.exception.OfertaNoEncontradaException;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;

public class AceptarOfertaService implements AceptarOfertaUseCase{
    private final OfertaRepositoryPort ofertaRepositoryPort;
    private final AsignacionViajePort asignacionViajePort;

    public AceptarOfertaService(
            OfertaRepositoryPort ofertaRepositoryPort,
            AsignacionViajePort asignacionViajePort) {
        this.ofertaRepositoryPort = ofertaRepositoryPort;
        this.asignacionViajePort = asignacionViajePort;
    }

    @Override
    public Oferta aceptar(String ofertaIdStr) {
        OfertaId ofertaId = new OfertaId(ofertaIdStr);
        Oferta oferta = ofertaRepositoryPort.buscarPorId(ofertaId)
                .orElseThrow(() -> new OfertaNoEncontradaException(ofertaIdStr));

        oferta.aceptar();
        Oferta guardada = ofertaRepositoryPort.guardar(oferta);

        asignacionViajePort.asignar(guardada.getViajeId().value(), guardada.getConductorId());

        return guardada;
    }
}
