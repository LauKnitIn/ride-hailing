package com.ride_hailing.Conductores.infrastructure.peristence.mapper;

import org.springframework.stereotype.Component;

import com.ride_hailing.Conductores.domain.Conductor;
import com.ride_hailing.Conductores.domain.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.DriverId;
import com.ride_hailing.Conductores.domain.EstadoDisponibilidad;
import com.ride_hailing.Conductores.domain.TipoDocumento;
import com.ride_hailing.Conductores.domain.UbicacionGeografica;

@Component 
public class ConductorPersistenceMapper {
    /**
     * Convierte del Agregado de Dominio a la Entidad JPA para guardar en base de datos.
     */
    public ConductorEntity toEntity(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        ConductorEntity entity = new ConductorEntity();
        entity.setIdConductor(conductor.getIdConductor().value());
        entity.setTipoDocumento(conductor.getDocumentoIdentidad().getTipoDocumento());
        entity.setNumeroDocumento(conductor.getDocumentoIdentidad().getNumeroDocumento());
        entity.setNombreCompleto(conductor.getNombreCompleto());
        entity.setFechaNacimiento(conductor.getFechaNacimiento());
        entity.setEstadoDisponibilidad(conductor.getDisponibilidad().name());

        if (conductor.getUbicacionActual() != null) {
            entity.setLatitud(conductor.getUbicacionActual().latitud());
            entity.setLongitud(conductor.getUbicacionActual().longitud());
        }

        return entity;
    }

    public Conductor toDomain(ConductorEntity entity) {
        if (entity == null) {
            return null;
        }

        // 1. Reconstruir Value Objects
        DriverId driverId = new DriverId(entity.getIdConductor());
        
        TipoDocumento tipoDoc = TipoDocumento.valueOf(entity.getTipoDocumento());
        DocumentoIdentidad documento = new DocumentoIdentidad(tipoDoc, entity.getNumeroDocumento());

        Conductor conductor = new Conductor(
            driverId,
            documento,
            entity.getNombreCompleto(),
            entity.getFechaNacimiento()
        );

        if (entity.getLatitud() != null && entity.getLongitud() != null) {
            UbicacionGeografica ubicacion = new UbicacionGeografica(entity.getLatitud(), entity.getLongitud());
            conductor.actualizarUbicacion(ubicacion);
        }

        EstadoDisponibilidad estado = EstadoDisponibilidad.valueOf(entity.getEstadoDisponibilidad());
        if (estado == EstadoDisponibilidad.DISPONIBLE) {
            conductor.registrarDisponibilidad();
        } else if (estado == EstadoDisponibilidad.INACTIVO) {
            conductor.desactivarDisponibilidad();
        }

        return conductor;
    }
}
