package com.ride_hailing.tarifas.infraestructure.entrada.web;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.application.DatosViajeTarifa;

@RestController
@RequestMapping("/internal/viajes")
public class TarifaEventoController {

    private final CalculoTarifaUseCase calculoTarifa;

    public TarifaEventoController(CalculoTarifaUseCase calculoTarifa) {
        this.calculoTarifa = calculoTarifa;
    }

    @PostMapping("/{viajeId}/finalizado")
    public ResponseEntity<Void> registrarFinalizacion(
            @PathVariable UUID viajeId,
            @Valid @RequestBody ViajeFinalizadoRequest request) {
        calculoTarifa.registrarViajeFinalizado(
                viajeId, new DatosViajeTarifa(request.distanciaKm(), request.tiempoMinutos()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{viajeId}/cancelado")
    public ResponseEntity<Void> registrarCancelacion(@PathVariable UUID viajeId) {
        calculoTarifa.registrarCargoParcial(viajeId);
        return ResponseEntity.noContent().build();
    }

    public record ViajeFinalizadoRequest(
            @DecimalMin(value = "0.0", message = "La distancia no puede ser negativa")
            double distanciaKm,
            @DecimalMin(value = "0.0", message = "La duración no puede ser negativa")
            double tiempoMinutos
    ) {
    }
}
