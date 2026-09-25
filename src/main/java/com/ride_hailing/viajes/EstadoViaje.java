package com.ride_hailing.viajes;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public record EstadoViaje(Valor valor) {

    public enum Valor {
        SOLICITADO, ASIGNADO, EN_CURSO, FINALIZADO, CANCELADO
    }
 
    private static final Map<Valor, Set<Valor>> TRANSICIONES_VALIDAS = new EnumMap<>(Valor.class);
    static {
        TRANSICIONES_VALIDAS.put(Valor.SOLICITADO, Set.of(Valor.ASIGNADO, Valor.CANCELADO));
        TRANSICIONES_VALIDAS.put(Valor.ASIGNADO, Set.of(Valor.EN_CURSO, Valor.CANCELADO));
        TRANSICIONES_VALIDAS.put(Valor.EN_CURSO, Set.of(Valor.FINALIZADO, Valor.CANCELADO));
        TRANSICIONES_VALIDAS.put(Valor.FINALIZADO, Set.of());
        TRANSICIONES_VALIDAS.put(Valor.CANCELADO, Set.of());
    }
 
    /** Constructor compacto: valida el invariante del Value Object. */
    public EstadoViaje {
        if (valor == null) {
            throw new IllegalArgumentException("El estado del viaje no puede ser nulo");
        }
    }
 
    public static EstadoViaje solicitado() {
        return new EstadoViaje(Valor.SOLICITADO);
    }
 
    public boolean permiteTransicionA(Valor destino) {
        return TRANSICIONES_VALIDAS.getOrDefault(this.valor, Set.of()).contains(destino);
    }
 
    /**
     * Intenta transicionar a un nuevo estado. Devuelve una nueva instancia si la
     * transición es válida; lanza {@link EstadoViajeInvalidoException} si no lo es.
     */
    public EstadoViaje transicionarA(Valor nuevoValor) {
        if (!permiteTransicionA(nuevoValor)) {
            throw new EstadoViajeInvalidoException(
                "No se puede pasar de " + this.valor + " a " + nuevoValor);
        }
        return new EstadoViaje(nuevoValor);
    }
 
    public boolean esFinal() {
        return valor == Valor.FINALIZADO || valor == Valor.CANCELADO;
    }
    
}
