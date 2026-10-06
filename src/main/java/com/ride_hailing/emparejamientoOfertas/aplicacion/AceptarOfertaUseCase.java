package com.ride_hailing.emparejamientoOfertas.aplicacion;

import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@Service
public class AceptarOfertaUseCase {

    private final OfertaRepository ofertaRepository;

    public AceptarOfertaUseCase(OfertaRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }

    public void ejecutar(UUID ofertaId) {
        Oferta oferta = ofertaRepository.buscarPorId(new OfertaId(ofertaId))
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

        oferta.aceptar();
        ofertaRepository.guardar(oferta);

        // AQUÍ: Notificar a Viajes que el conductor aceptó (ej. evento de dominio / puerto)
    }
}