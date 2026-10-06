package com.ride_hailing.calificacion.domain;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Calificacion {

   private Long id;
   private Long viajeId;
   private Long pasajeroId;
   private Long conductorId;
   private Puntuacion puntuacion; 
   private String comentario;
   private Instant fechaCreacion;

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
