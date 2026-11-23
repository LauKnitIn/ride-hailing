package com.ride_hailing.calificacion.infraestructure.salida.persistence;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** La restriccion UNIQUE sobre viajeId garantiza a nivel de BD que un viaje no se califique dos veces. */
@Entity
@Table(name = "calificaciones")
class CalificacionJpaEntity {

    @Id
    UUID id;
    @Column(nullable = false, unique = true)
    UUID viajeId;
    UUID pasajeroId;
    UUID conductorId;
    int puntuacion;
    String comentario;
    Instant fechaCreacion;

    protected CalificacionJpaEntity() {
    }
}
