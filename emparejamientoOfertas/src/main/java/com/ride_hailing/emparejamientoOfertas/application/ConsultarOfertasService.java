package com.ride_hailing.emparejamientoOfertas.application;

import java.util.List;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ConsultarOfertasUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.exception.OfertaNoEncontradaException;
import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;
import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;

public class ConsultarOfertasService implements ConsultarOfertasUseCase {

    private final OfertaRepositoryPort ofertaRepositoryPort;

    public ConsultarOfertasService(OfertaRepositoryPort ofertaRepositoryPort) {
        this.ofertaRepositoryPort = ofertaRepositoryPort;
    }

    @Override
    public Oferta porId(String ofertaId) {
        return ofertaRepositoryPort.buscarPorId(new OfertaId(ofertaId))
                .orElseThrow(() -> new OfertaNoEncontradaException(ofertaId));
    }

    @Override
    public List<Oferta> porViaje(String viajeId) {
        return ofertaRepositoryPort.buscarPorViajeId(new ViajeId(viajeId));
    }

    @Override
    public List<Oferta> pendientesDelConductor(String conductorId) {
        return ofertaRepositoryPort.buscarPorConductorIdyEstado(conductorId, EstadoOferta.PROPUESTA);
    }
}
