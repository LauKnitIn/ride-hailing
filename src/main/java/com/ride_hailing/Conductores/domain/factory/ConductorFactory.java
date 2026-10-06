package com.ride_hailing.Conductores.domain.factory;

import java.time.LocalDate;
import java.util.UUID;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.model.DriverId;
import com.ride_hailing.Conductores.domain.model.TipoDocumento;

public class ConductorFactory {
    
    public static Conductor crearConductor(
            String tipoDocumentoStr,
            String numeroDocumento,
            String nombreCompleto,
            LocalDate fechaNacimiento
    ) {
        // 1. Generación de identidad única del agregado
        DriverId driverId = new DriverId(UUID.randomUUID().toString());

        // 2. Construcción de Value Objects con validación de tipo
        TipoDocumento tipoDocumento = parsearTipoDocumento(tipoDocumentoStr);
        DocumentoIdentidad documento = new DocumentoIdentidad(numeroDocumento, tipoDocumento);

        // 3. Creación y devolución de la entidad raíz (las validaciones internas como mayoría de edad las ejecuta el constructor de Conductor)
        return new Conductor(
            driverId,
            documento,
            nombreCompleto,
            fechaNacimiento
        );
    }

    public static Conductor reconstruirConductor(
            String idConductor,
            String tipoDocumentoStr,
            String numeroDocumento,
            String nombreCompleto,
            LocalDate fechaNacimiento
    ) {
        DriverId driverId = new DriverId(idConductor);
        TipoDocumento tipoDocumento = parsearTipoDocumento(tipoDocumentoStr);
        DocumentoIdentidad documento = new DocumentoIdentidad(numeroDocumento, tipoDocumento);

        return new Conductor(
            driverId,
            documento,
            nombreCompleto,
            fechaNacimiento
        );
    }

    private static TipoDocumento parsearTipoDocumento(String tipoStr) {
        try {
            return TipoDocumento.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("El tipo de documento '" + tipoStr + "' no es válido.");
        }
    }
}
