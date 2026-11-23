package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ride_hailing.emparejamientoOfertas.application.ReglaDeNegocioViajeException;
import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.application.ViajeNoEncontradoException;
import com.ride_hailing.emparejamientoOfertas.domain.exception.OfertaNoEncontradaException;

@RestControllerAdvice
public class ManejadorErroresEmparejamiento {

    @ExceptionHandler(OfertaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RuntimeException e) {
        return respuesta(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(ViajeNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> viajeNoEncontrado(RuntimeException e) {
        return respuesta(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflicto(RuntimeException e) {
        return respuesta(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(ReglaDeNegocioViajeException.class)
    public ResponseEntity<Map<String, String>> reglaNegocio(RuntimeException e) {
        return respuesta(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> peticionInvalida(RuntimeException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(ServicioNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> servicioNoDisponible(RuntimeException e) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, e);
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus status, RuntimeException e) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(e.getMessage())));
    }
}
