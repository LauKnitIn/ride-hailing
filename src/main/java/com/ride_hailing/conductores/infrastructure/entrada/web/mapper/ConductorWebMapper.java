package com.ride_hailing.conductores.infrastructure.entrada.web.mapper;

import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.TipoDocumento;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.conductores.infrastructure.entrada.web.dto.ConductorResponse;
import com.ride_hailing.conductores.infrastructure.entrada.web.dto.UbicacionRequest;

@Component 
public class ConductorWebMapper {

    public ConductorResponse aRespuesta(Conductor c) {
        UbicacionGeografica u = c.getUbicacionActual();
return new ConductorResponse(
        String.valueOf(c.getIdConductor().value()), c.getNombreCompleto(),
        c.getDocumentoIdentidad().getTipoDocumento().toString(), c.getDocumentoIdentidad().getNumeroDocumento(),
        c.getFechaNacimiento(), c.getDisponibilidad().name(),
        u == null ? null : u.latitud(), u == null ? null : u.longitud());
    }

    public UbicacionGeografica aUbicacion(UbicacionRequest r) {
        return new UbicacionGeografica(r.latitud(), r.longitud());
    }

    public TipoDocumento aTipoDocumento(String valor) {
        return TipoDocumento.valueOf(valor);
    }

}
