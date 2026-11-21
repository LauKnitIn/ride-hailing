package com.ride_hailing.viajes.infraestructura.entrada.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ride_hailing.viajes.aplicacion.ConductorNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.ViajeNoEncontradoException;
import com.ride_hailing.viajes.dominio.EstadoViajeInvalidoException;

@RestControllerAdvice
public class ManejadorErroresViajes {

    @ExceptionHandler(ViajeNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RuntimeException e) {
        return respuesta(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({EstadoViajeInvalidoException.class, ConductorNoDisponibleException.class,
            IllegalStateException.class})
    public ResponseEntity<Map<String, String>> conflicto(RuntimeException e) {
        return respuesta(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> peticionInvalida(RuntimeException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus status, RuntimeException e) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(e.getMessage())));
    }
}
