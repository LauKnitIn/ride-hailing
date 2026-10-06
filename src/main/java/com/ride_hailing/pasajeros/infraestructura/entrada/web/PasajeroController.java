package com.ride_hailing.pasajeros.infraestructura.entrada.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ride_hailing.pasajeros.aplicacion.PasajeroUseCase;
import com.ride_hailing.pasajeros.dominio.Pasajero;

import java.util.UUID;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroUseCase pasajeroUseCase;

    public PasajeroController(PasajeroUseCase pasajeroUseCase) {
        this.pasajeroUseCase = pasajeroUseCase;
    }

    @PostMapping
    public ResponseEntity<PasajeroResponse> registrar(@RequestBody RegistrarPasajeroRequest request) {
        Pasajero pasajero = pasajeroUseCase.registrar(request.nombre(), request.correo(), request.telefono());
        return ResponseEntity.ok(PasajeroResponse.desde(pasajero));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroResponse> consultar(@PathVariable UUID id) {
        return ResponseEntity.ok(PasajeroResponse.desde(pasajeroUseCase.consultar(id)));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<Void> actualizarPerfil(@PathVariable UUID id, @RequestBody RegistrarPasajeroRequest request) {
        pasajeroUseCase.actualizarPerfil(id, request.nombre(), request.telefono());
        return ResponseEntity.noContent().build();
    }

}
