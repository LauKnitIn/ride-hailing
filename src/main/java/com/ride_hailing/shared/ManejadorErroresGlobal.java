package com.ride_hailing.shared;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ride_hailing.calificacion.domain.CalificacionDuplicadaException;
import com.ride_hailing.calificacion.domain.PasajeroNoAutorizadoException;
import com.ride_hailing.calificacion.domain.ViajeNoCalificableException;
import com.ride_hailing.conductores.domain.excepcion.ConductorConViajeActivoException;
import com.ride_hailing.conductores.domain.excepcion.ConductorDuplicadoException;
import com.ride_hailing.conductores.domain.excepcion.ConductorNoEncontradoException;
import com.ride_hailing.emparejamientoOfertas.domain.exception.OfertaNoEncontradaException;
import com.ride_hailing.viajes.aplicacion.ConductorNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.ViajeNoEncontradoException;
import com.ride_hailing.viajes.dominio.EstadoViajeInvalidoException;

/** Traduce las excepciones de dominio a respuestas HTTP coherentes en todos los modulos. */
@RestControllerAdvice
public class ManejadorErroresGlobal {

    @ExceptionHandler({ViajeNoEncontradoException.class, ConductorNoEncontradoException.class,
            OfertaNoEncontradaException.class})
    public ResponseEntity<Map<String, String>> noEncontrado(RuntimeException e) {
        return respuesta(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({EstadoViajeInvalidoException.class, ConductorNoDisponibleException.class,
            ConductorDuplicadoException.class, ConductorConViajeActivoException.class,
            CalificacionDuplicadaException.class, ViajeNoCalificableException.class,
            IllegalStateException.class})
    public ResponseEntity<Map<String, String>> conflicto(RuntimeException e) {
        return respuesta(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(PasajeroNoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> prohibido(RuntimeException e) {
        return respuesta(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> peticionInvalida(RuntimeException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus status, RuntimeException e) {
        return ResponseEntity.status(status).body(Map.of("error", String.valueOf(e.getMessage())));
    }
}
