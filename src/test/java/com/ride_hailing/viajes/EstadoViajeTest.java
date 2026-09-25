package com.ride_hailing.viajes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EstadoViajeTest {

    @Test
    void unViajeNuevoEmpiezaSolicitado() {
        EstadoViaje estado = EstadoViaje.solicitado();
        assertEquals(EstadoViaje.Valor.SOLICITADO, estado.valor());
    }
 
    @Test
    void noSePuedeCrearUnEstadoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new EstadoViaje(null));
    }
 
    @Test
    void solicitadoPuedeTransicionarAAsignado() {
        EstadoViaje estado = EstadoViaje.solicitado();
        EstadoViaje nuevo = estado.transicionarA(EstadoViaje.Valor.ASIGNADO);
        assertEquals(EstadoViaje.Valor.ASIGNADO, nuevo.valor());
    }
 
    @Test
    void solicitadoPuedeCancelarseDirectamente() {
        EstadoViaje estado = EstadoViaje.solicitado();
        EstadoViaje nuevo = estado.transicionarA(EstadoViaje.Valor.CANCELADO);
        assertEquals(EstadoViaje.Valor.CANCELADO, nuevo.valor());
    }
 
    @Test
    void noSePuedeSaltarDeSolicitadoAEnCurso() {
        EstadoViaje estado = EstadoViaje.solicitado();
        assertThrows(EstadoViajeInvalidoException.class,
            () -> estado.transicionarA(EstadoViaje.Valor.EN_CURSO));
    }
 
    @Test
    void finalizadoEsUnEstadoTerminal() {
        EstadoViaje estado = new EstadoViaje(EstadoViaje.Valor.FINALIZADO);
        assertTrue(estado.esFinal());
        assertThrows(EstadoViajeInvalidoException.class,
            () -> estado.transicionarA(EstadoViaje.Valor.CANCELADO));
    }
 
    @Test
    void canceladoEsUnEstadoTerminal() {
        EstadoViaje estado = new EstadoViaje(EstadoViaje.Valor.CANCELADO);
        assertTrue(estado.esFinal());
        assertThrows(EstadoViajeInvalidoException.class,
            () -> estado.transicionarA(EstadoViaje.Valor.EN_CURSO));
    }
    
}
