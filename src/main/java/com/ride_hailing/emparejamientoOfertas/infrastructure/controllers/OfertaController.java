package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.emparejamientoOfertas.aplicacion.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;

@RestController
@RequestMapping("/api/v1/emparejamiento")
public class OfertaController {

    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudUseCase;
    private final AceptarOfertaUseCase aceptarOfertaUseCase;
    private final RechazarOfertaUseCase rechazarOfertaUseCase;

    public OfertaController(
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudUseCase,
            AceptarOfertaUseCase aceptarOfertaUseCase,
            RechazarOfertaUseCase rechazarOfertaUseCase) {
        this.procesarSolicitudUseCase = procesarSolicitudUseCase;
        this.aceptarOfertaUseCase = aceptarOfertaUseCase;
        this.rechazarOfertaUseCase = rechazarOfertaUseCase;
    }

    public record SolicitudEmparejamientoRequest(UUID viajeId, double origenLat, double origenLon) {}

    @PostMapping("/solicitar")
    public ResponseEntity<Oferta> solicitar(@RequestBody SolicitudEmparejamientoRequest request) {
        Oferta oferta = procesarSolicitudUseCase.ejecutar(
                request.viajeId(), request.origenLat(), request.origenLon());

        if (oferta == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(oferta);
    }

    @PostMapping("/{id}/aceptar")
    public ResponseEntity<Void> aceptar(@PathVariable UUID id) {
        aceptarOfertaUseCase.ejecutar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<Void> rechazar(@PathVariable UUID id) {
        rechazarOfertaUseCase.ejecutar(id);
        return ResponseEntity.ok().build();
    }
}
