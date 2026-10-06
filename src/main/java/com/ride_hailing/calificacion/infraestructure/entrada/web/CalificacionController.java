package com.ride_hailing.calificacion.infraestructure.entrada.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.calificacion.application.CalificarConductorUseCase;
import com.ride_hailing.calificacion.domain.Calificacion;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    private final CalificarConductorUseCase calificarConductor;

    public CalificacionController(CalificarConductorUseCase calificarConductor) {
        this.calificarConductor = calificarConductor;
    }

    @PostMapping
    public ResponseEntity<CalificacionResponse> calificar(
            @RequestBody CalificarConductorRequest request) {
        Calificacion calificacion = calificarConductor.calificarConductor(
                request.viajeId(),
                request.puntuacion(),
                request.comentario()
        );
        return ResponseEntity.ok(CalificacionResponse.desde(calificacion));
    }
}
