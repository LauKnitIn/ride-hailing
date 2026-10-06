package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.emparejamientoOfertas.aplicacion.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.ResponderOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.infrastructure.controllers.dto.RespuestaOfertaRequest;
import com.ride_hailing.emparejamientoOfertas.infrastructure.controllers.dto.SolicitudEmparejamientoRequest;

@RestController
@RequestMapping("/api/v1/emparejamiento")
public class EmparejamientoRestController {
    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase;
    private final ResponderOfertaUseCase responderOfertaUseCase;

    public EmparejamientoRestController(
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase,
            ResponderOfertaUseCase responderOfertaUseCase) {
        this.procesarSolicitudEmparejamientoUseCase = procesarSolicitudEmparejamientoUseCase;
        this.responderOfertaUseCase = responderOfertaUseCase;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitarEmparejamiento(@RequestBody SolicitudEmparejamientoRequest request) {
        procesarSolicitudEmparejamientoUseCase.ejecutar(
                request.viajeId(),
                request.origenLat(),
                request.origenLon()
        );
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/ofertas/{ofertaId}/responder")
    public ResponseEntity<Void> responderOferta(
            @PathVariable UUID ofertaId,
            @RequestBody RespuestaOfertaRequest request) {
        responderOfertaUseCase.responder(
                new OfertaId(ofertaId),
                request.aceptada(),
                request.origenLat(),
                request.origenLon()
        );
        return ResponseEntity.ok().build();
    }
}
