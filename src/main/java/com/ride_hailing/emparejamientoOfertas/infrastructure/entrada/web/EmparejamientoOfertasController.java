package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;

@RestController
@RequestMapping("/api")
public class EmparejamientoOfertasController {

    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase;
    private final AceptarOfertaUseCase aceptarOfertaUseCase;
    private final RechazarOfertaUseCase rechazarOfertaUseCase;
    private final EmparejamientoWebMapper webMapper;

    public EmparejamientoOfertasController(
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase,
            AceptarOfertaUseCase aceptarOfertaUseCase,
            RechazarOfertaUseCase rechazarOfertaUseCase,
            EmparejamientoWebMapper webMapper) {
        this.procesarSolicitudEmparejamientoUseCase = procesarSolicitudEmparejamientoUseCase;
        this.aceptarOfertaUseCase = aceptarOfertaUseCase;
        this.rechazarOfertaUseCase = rechazarOfertaUseCase;
        this.webMapper = webMapper;
    }

    @PostMapping("/emparejamientos")
    public ResponseEntity<OfertaResponse> procesarEmparejamiento(@RequestBody SolicitudEmparejamientoRequest request) {
        Optional<Oferta> ofertaOpt = procesarSolicitudEmparejamientoUseCase.procesar(
                request.viajeId(),
                request.origenLatitud(),
                request.origenLongitud()
        );

        return ofertaOpt
                .map(oferta -> ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponse(oferta)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping("/ofertas/{id}/aceptar")
    public ResponseEntity<OfertaResponse> aceptarOferta(@PathVariable("id") String id) {
        Oferta ofertaAceptada = aceptarOfertaUseCase.aceptar(id);
        return ResponseEntity.ok(webMapper.toResponse(ofertaAceptada));
    }

    @PostMapping("/ofertas/{id}/rechazar")
    public ResponseEntity<OfertaResponse> rechazarOferta(
            @PathVariable("id") String id,
            @RequestParam("origenLatitud") double origenLatitud,
            @RequestParam("origenLongitud") double origenLongitud) {
        Optional<Oferta> nuevaOferta = rechazarOfertaUseCase.rechazar(id, origenLatitud, origenLongitud);

        return nuevaOferta
                .map(oferta -> ResponseEntity.ok(webMapper.toResponse(oferta)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
