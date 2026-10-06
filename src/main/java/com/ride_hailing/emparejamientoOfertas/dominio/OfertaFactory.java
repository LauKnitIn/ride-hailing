package com.ride_hailing.emparejamientoOfertas.dominio;

import java.time.Instant;
import java.util.UUID;

public class OfertaFactory {

    public static Oferta crearNuevaOferta(UUID viajeId, UUID conductorId, double origenLat, double origenLon) {
        return new Oferta(
                OfertaId.generar(),
                viajeId,
                conductorId,
                origenLat,
                origenLon,
                EstadoOferta.PENDIENTE,
                Instant.now()
        );
    }

    public static Oferta reconstruir(UUID id, UUID viajeId, UUID conductorId, double origenLat, double origenLon, EstadoOferta estado, Instant fechaCreacion) {
        return new Oferta(
                new OfertaId(id),
                viajeId,
                conductorId,
                origenLat,
                origenLon,
                estado,
                fechaCreacion
        );
    }
}