package com.ride_hailing.tarifas.infraestructure.entrada.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.infraestructure.salida.persistence.TarifasCalculadas;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    private final CalculoTarifaUseCase calculoTarifa;

    private final TarifasCalculadas tarifasCalculadas;

    public TarifaController(CalculoTarifaUseCase calculoTarifa, TarifasCalculadas tarifasCalculadas) {
        this.calculoTarifa = calculoTarifa;
        this.tarifasCalculadas = tarifasCalculadas;
    }

    @GetMapping("/{viajeId}")
    public ResponseEntity<TarifaResponse> consultar(@PathVariable UUID viajeId) {
        return tarifasCalculadas.buscar(viajeId)
                .map(monto -> ResponseEntity.ok(TarifaResponse.desde(viajeId, monto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{viajeId}/cargo-parcial")
    public ResponseEntity<TarifaResponse> cargoParcial(@PathVariable UUID viajeId) {
        return tarifasCalculadas.buscarCargoParcial(viajeId)
                .map(monto -> ResponseEntity.ok(TarifaResponse.desde(viajeId, monto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{viajeId}/calcular")
    public ResponseEntity<TarifaResponse> calcular(@PathVariable UUID viajeId) {
        Monto monto = calculoTarifa.calcularTarifa(viajeId);
        return ResponseEntity.ok(TarifaResponse.desde(viajeId, monto));
    }
}
