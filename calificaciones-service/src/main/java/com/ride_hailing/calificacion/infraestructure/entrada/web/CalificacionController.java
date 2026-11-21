package com.ride_hailing.calificacion.infraestructure.entrada.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.calificacion.application.CalificarConductorUseCase;
import com.ride_hailing.calificacion.application.ConsultarCalificacionesUseCase;
import com.ride_hailing.calificacion.domain.Calificacion;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    private final CalificarConductorUseCase calificarConductor;
    private final ConsultarCalificacionesUseCase consultarCalificaciones;

    public CalificacionController(
            CalificarConductorUseCase calificarConductor,
            ConsultarCalificacionesUseCase consultarCalificaciones) {
        this.calificarConductor = calificarConductor;
        this.consultarCalificaciones = consultarCalificaciones;
    }

    @GetMapping("/conductor/{conductorId}")
    public ResponseEntity<List<CalificacionConductorResponse>> consultarPorConductor(
            @PathVariable UUID conductorId) {
        List<CalificacionConductorResponse> calificaciones = consultarCalificaciones
                .consultarPorConductor(conductorId)
                .stream()
                .map(CalificacionConductorResponse::desde)
                .toList();
        return ResponseEntity.ok(calificaciones);
    }

    @PostMapping
    public ResponseEntity<CalificacionResponse> calificar(@RequestBody CalificarConductorRequest request) {
        Calificacion calificacion = calificarConductor.calificarConductor(
                request.viajeId(),
                request.pasajeroId(),
                request.puntuacion(),
                request.comentario());
        return ResponseEntity.ok(CalificacionResponse.desde(calificacion));
    }
}
