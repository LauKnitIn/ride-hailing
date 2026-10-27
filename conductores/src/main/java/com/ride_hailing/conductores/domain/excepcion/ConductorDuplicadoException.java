package com.ride_hailing.conductores.domain.excepcion;

import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;

public class ConductorDuplicadoException extends RuntimeException {
    public ConductorDuplicadoException(DocumentoIdentidad documento) {
        super("Ya existe un conductor con el documento " + documento.getTipoDocumento() + " " + documento.getNumeroDocumento());
    }
}
