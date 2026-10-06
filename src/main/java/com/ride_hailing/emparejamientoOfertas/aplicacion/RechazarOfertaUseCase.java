package com.ride_hailing.emparejamientoOfertas.aplicacion;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@Service
public class RechazarOfertaUseCase {

    private final OfertaRepository ofertaRepository;
    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudUseCase;

    public RechazarOfertaUseCase(
            OfertaRepository ofertaRepository,
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudUseCase) {
        this.ofertaRepository = ofertaRepository;
        this.procesarSolicitudUseCase = procesarSolicitudUseCase;
    }

    public void ejecutar(UUID ofertaId) {
        Oferta oferta = ofertaRepository.buscarPorId(new OfertaId(ofertaId))
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

        oferta.rechazar();
        ofertaRepository.guardar(oferta);

        procesarSolicitudUseCase.ejecutar(
                oferta.getViajeId(),
                oferta.getOrigenLat(),
                oferta.getOrigenLon()
        );
    }
}