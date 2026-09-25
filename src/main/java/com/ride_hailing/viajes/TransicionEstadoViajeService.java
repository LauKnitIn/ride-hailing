package com.ride_hailing.viajes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Paso 3 · Servicio de Dominio.
 *
 * Existe porque dos reglas de negocio del documento de requisitos NO
 * pertenecen naturalmente a una sola instancia de {@link Viaje}:
 *
 * <ol>
 *   <li>"Un conductor no puede recibir una nueva solicitud mientras tiene un
 *       viaje en curso" — para decidirlo hace falta mirar TODOS los viajes
 *       activos de ese conductor, no solo el viaje que se está asignando.</li>
 *   <li>"Un viaje cancelado después de la asignación puede generar un cargo
 *       parcial al pasajero" — depende de en qué estado estaba el viaje ANTES
 *       de cancelarlo, y el resultado cruza hacia el subdominio de Tarifas
 *       (Dev 3), por eso este servicio solo devuelve la señal
 *       ({@code requiereCargoParcial}), no calcula el monto.</li>
 * </ol>
 */
public class TransicionEstadoViajeService {

    /**
     * Asigna un conductor a un viaje, validando que no tenga otro viaje en curso.
     *
     * @param viaje                     viaje que se va a asignar (en estado SOLICITADO)
     * @param conductorId               id del conductor candidato (referencia por id,
     *                                  el objeto Conductor pertenece al subdominio de Dev 1)
     * @param viajesActivosDelConductor viajes de ese conductor que el llamador ya cargó
     *                                  (p. ej. desde un repositorio), para poder
     *                                  validar la regla sin que Viaje conozca a los demás
     */
    public void asignarConductor(Viaje viaje, UUID conductorId,
                                  List<Viaje> viajesActivosDelConductor, LocalDateTime momento) {
        boolean conductorOcupado = viajesActivosDelConductor.stream()
            .anyMatch(v -> v.getEstado().valor() == EstadoViaje.Valor.EN_CURSO);
        if (conductorOcupado) {
            throw new ConductorNoDisponibleException(
                "El conductor " + conductorId + " ya tiene un viaje en curso");
        }
        viaje.asignarConductor(conductorId, momento);
    }

    /**
     * Cancela un viaje y determina si corresponde un cargo parcial, según en qué
     * estado estaba antes de cancelarse.
     */
    public ResultadoCancelacion cancelar(Viaje viaje, String motivo) {
        EstadoViaje.Valor estadoPrevio = viaje.getEstado().valor();
        viaje.cancelar(motivo);

        boolean requiereCargoParcial =
            estadoPrevio == EstadoViaje.Valor.ASIGNADO || estadoPrevio == EstadoViaje.Valor.EN_CURSO;

        return new ResultadoCancelacion(viaje.getId(), requiereCargoParcial);
    }

    /**
     * Resultado de una cancelación. {@code requiereCargoParcial} es la señal que
     * el subdominio de Tarifas (Dev 3) necesita para decidir si cobra algo —
     * el monto en sí no se calcula aquí.
     */
    public record ResultadoCancelacion(UUID viajeId, boolean requiereCargoParcial) {}
}
