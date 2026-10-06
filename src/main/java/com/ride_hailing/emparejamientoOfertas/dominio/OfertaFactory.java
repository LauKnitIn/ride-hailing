package com.ride_hailing.emparejamientoOfertas.dominio;

import java.time.Instant;
import java.util.UUID;

public class OfertaFactory {
    /**
     * Crear una nueva oferta de viaje (nueva en el sistema)
     */
    public static Oferta crearNuevaOferta(UUID viajeId, UUID conductorId) {
        return new Oferta(OfertaId.generar(), viajeId, conductorId);
    }


    /**
     * Reconstruir una oferta existente desde la capa de infraestructura (JPA/Base de Datos)
     */
    public static Oferta reconstruir(UUID id, UUID viajeId, UUID conductorId, EstadoOferta estado, Instant fechaCreacion) {
        return new Oferta(
                new OfertaId(id),
                viajeId,
                conductorId,
                estado,
                fechaCreacion
        );
    }
}
