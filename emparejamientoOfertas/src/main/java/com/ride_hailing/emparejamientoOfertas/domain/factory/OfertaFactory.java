package com.ride_hailing.emparejamientoOfertas.domain.factory;

import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.OfertaId;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

public class OfertaFactory {
    public Oferta crearOfertaPropuesta(ViajeId viajeId, String conductorId,
                                       double origenLatitud, double origenLongitud) {
        return new Oferta(
                OfertaId.generar(),
                viajeId,
                conductorId,
                origenLatitud,
                origenLongitud,
                EstadoOferta.PROPUESTA
        );
    }
}
