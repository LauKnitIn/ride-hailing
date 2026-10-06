package com.ride_hailing.emparejamientoOfertas.aplicacion;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.emparejamientoOfertas.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@Service 
public class AceptarOfertaUseCase {

    private final OfertaRepository ofertaRepository;
    private final AsignacionViajePort asignacionViajePort;

    public AceptarOfertaUseCase(OfertaRepository ofertaRepository, AsignacionViajePort asignacionViajePort) {
        this.ofertaRepository = ofertaRepository;
        this.asignacionViajePort = asignacionViajePort;
    }

    public void ejecutar(UUID ofertaId) {
        Oferta oferta = ofertaRepository.buscarPorId(new OfertaId(ofertaId))
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada: " + ofertaId));

        oferta.aceptar();
        ofertaRepository.guardar(oferta);

        asignacionViajePort.notificarConductorAsignado(
                oferta.getViajeId(),
                oferta.getConductorId(),
                Instant.now()
        );
    }
}