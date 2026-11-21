package com.ride_hailing.calificacion.infraestructure.entrada.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ride_hailing.calificacion.application.ServicioViajesNoDisponibleException;
import com.ride_hailing.calificacion.domain.CalificacionDuplicadaException;
import com.ride_hailing.calificacion.domain.PasajeroNoAutorizadoException;
import com.ride_hailing.calificacion.domain.ViajeNoCalificableException;
import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;

@RestControllerAdvice
public class CalificacionesErrorHandler {

    @ExceptionHandler(ViajeNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RuntimeException exception) {
        return respuesta(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler({CalificacionDuplicadaException.class, ViajeNoCalificableException.class})
    public ResponseEntity<Map<String, String>> conflicto(RuntimeException exception) {
        return respuesta(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(PasajeroNoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> prohibido(RuntimeException exception) {
        return respuesta(HttpStatus.FORBIDDEN, exception);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> peticionInvalida(RuntimeException exception) {
        return respuesta(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(ServicioViajesNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> servicioViajesNoDisponible(
            ServicioViajesNoDisponibleException exception) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, exception);
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus status, RuntimeException exception) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(exception.getMessage())));
    }
}
