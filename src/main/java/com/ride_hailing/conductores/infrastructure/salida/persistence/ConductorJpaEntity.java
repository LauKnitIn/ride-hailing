package com.ride_hailing.conductores.infrastructure.salida.persistence;


import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;
import com.ride_hailing.conductores.domain.model.TipoDocumento;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;



@Entity
@Table(name = "conductores")
class ConductorJpaEntity {

    @Id
    String id;
    String nombreCompleto;
    @Enumerated(EnumType.STRING)
    TipoDocumento tipoDocumento;
    String numeroDocumento;
    @Enumerated(EnumType.STRING)
    EstadoDisponibilidad estado;
    Double latitud;
    Double longitud;

    protected ConductorJpaEntity() {
    }
}

