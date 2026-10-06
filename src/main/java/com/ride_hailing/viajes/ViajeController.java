package com.ride_hailing.viajes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeUseCase viajeUseCase;

    public ViajeController(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @PostMapping
    public ResponseEntity<ViajeResponse> solicitar(@RequestBody SolicitarViajeRequest request) {
        Viaje viaje = viajeUseCase.solicitarViaje(
                request.pasajeroId(),
                request.latitudOrigen(), request.longitudOrigen(),
                request.latitudDestino(), request.longitudDestino());
        return ResponseEntity.ok(ViajeResponse.desde(viaje));
    }

    @PostMapping("/{id}/conductor/{conductorId}")
    public ResponseEntity<Void> asignarConductor(@PathVariable UUID id, @PathVariable UUID conductorId) {
        viajeUseCase.asignarConductor(id, conductorId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rechazar-sin-conductores")
    public ResponseEntity<Void> rechazarPorFaltaDeConductores(@PathVariable UUID id, @RequestBody String motivo) {
        viajeUseCase.rechazarPorFaltaDeConductores(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciar(@PathVariable UUID id) {
        viajeUseCase.iniciarViaje(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable UUID id) {
        viajeUseCase.finalizarViaje(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id, @RequestBody String motivo) {
        viajeUseCase.cancelarViaje(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponse> consultar(@PathVariable UUID id) {
        return ResponseEntity.ok(ViajeResponse.desde(viajeUseCase.consultarViaje(id)));
    }

    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<ViajeResponse>> historial(@PathVariable UUID pasajeroId) {
        List<ViajeResponse> respuesta = viajeUseCase.listarViajesDelPasajero(pasajeroId).stream()
                .map(ViajeResponse::desde)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
}
