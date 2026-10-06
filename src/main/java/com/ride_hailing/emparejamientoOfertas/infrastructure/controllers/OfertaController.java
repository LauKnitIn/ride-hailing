package com.ride_hailing.emparejamientoOfertas.infrastructure.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ride_hailing.emparejamientoOfertas.aplicacion.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.RechazarOfertaUseCase;

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

    public record SolicitudEmparejamientoDTO(UUID viajeId, double origenLat, double origenLon) {}

    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitar(@RequestBody SolicitudEmparejamientoDTO dto) {
        procesarSolicitudUseCase.ejecutar(dto.viajeId(), dto.origenLat(), dto.origenLon());
        return ResponseEntity.ok().build();
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
