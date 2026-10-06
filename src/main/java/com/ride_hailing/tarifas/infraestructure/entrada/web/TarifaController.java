package com.ride_hailing.tarifas.infraestructure.entrada.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.domain.Monto;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    private final CalculoTarifaUseCase calculoTarifa;

    public TarifaController(CalculoTarifaUseCase calculoTarifa) {
        this.calculoTarifa = calculoTarifa;
    }

    @PostMapping("/{viajeId}/calcular")
    public ResponseEntity<TarifaResponse> calcular(@PathVariable UUID viajeId) {
        Monto monto = calculoTarifa.calcularTarifa(viajeId);
        return ResponseEntity.ok(TarifaResponse.desde(viajeId, monto));
    }
}
