package com.ride_hailing.calificacion.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.ride_hailing.calificacion.infraestructure.salida.persistencia.PuntuacionConverter;

@Getter
@Setter
@Entity
@Table(name = "calificaciones")
public class Calificacion {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private Long viajeId;
   private Long pasajeroId;
   private Long conductorId;
   @Convert(converter = PuntuacionConverter.class)
   @Column(name = "puntuacion")
   private Puntuacion puntuacion;
   private String comentario;
   private Instant fechaCreacion;

   protected Calificacion() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getViajeId() {
      return viajeId;
   }

   public void setViajeId(Long viajeId) {
      this.viajeId = viajeId;
   }

   public Long getPasajeroId() {
      return pasajeroId;
   }

   public void setPasajeroId(Long pasajeroId) {
      this.pasajeroId = pasajeroId;
   }

   public Long getConductorId() {
      return conductorId;
   }

   public void setConductorId(Long conductorId) {
      this.conductorId = conductorId;
   }

   public Puntuacion getPuntuacion() {
      return puntuacion;
   }

   public void setPuntuacion(Puntuacion puntuacion) {
      this.puntuacion = puntuacion;
   }

   public String getComentario() {
      return comentario;
   }

   public void setComentario(String comentario) {
      this.comentario = comentario;
   }

   public Instant getFechaCreacion() {
      return fechaCreacion;
   }

   public void setFechaCreacion(Instant fechaCreacion) {
      this.fechaCreacion = fechaCreacion;
   }
}
