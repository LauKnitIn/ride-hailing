package com.ride_hailing.conductores.infrastructure.salida.persistence;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.TipoDocumento;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;

@Component
class ConductorPersistenceMapper {

    ConductorJpaEntity aEntidad(Conductor c) {
        ConductorJpaEntity e = new ConductorJpaEntity();
        e.id = c.getIdConductor().value();
        e.nombreCompleto = c.getNombreCompleto();
        e.tipoDocumento = aTipoDocumento(c.getDocumentoIdentidad().getTipoDocumento());   
        e.numeroDocumento = c.getDocumentoIdentidad().getNumeroDocumento();
        e.fechaNacimiento = c.getFechaNacimiento();                       
        e.estado = c.getDisponibilidad();
        UbicacionGeografica u = c.getUbicacionActual();
        e.latitud = u == null ? null : u.latitud();
        e.longitud = u == null ? null : u.longitud();
        return e;
    }

    Conductor aDominio(ConductorJpaEntity e) {
        UbicacionGeografica ubicacion = (e.latitud == null || e.longitud == null)
                ? null : new UbicacionGeografica(e.latitud, e.longitud);
        return Conductor.reconstruir(
                new DriverId(e.id),                                                        
                new DocumentoIdentidad(e.numeroDocumento, e.tipoDocumento),  
                e.nombreCompleto,
                e.fechaNacimiento,                                                          
                e.estado,
                ubicacion);
    }

    TipoDocumento aTipoDocumento(String texto) {
    for (TipoDocumento tipo : TipoDocumento.values()) {
        if (tipo.toString().equals(texto) || tipo.name().equals(texto)) {
            return tipo;
        }
    }
    throw new IllegalArgumentException("Tipo de documento desconocido: " + texto);
}
}
