package com.ride_hailing.Conductores.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.model.DriverId;
import com.ride_hailing.Conductores.domain.model.EstadoDisponibilidad;
import com.ride_hailing.Conductores.domain.model.TipoDocumento;
import com.ride_hailing.Conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.Conductores.infrastructure.persistence.entity.ConductorEntity;

@Component 
public class ConductorPersistenceMapper {

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
        DocumentoIdentidad documento = new DocumentoIdentidad(entity.getNumeroDocumento(),tipoDoc);

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
