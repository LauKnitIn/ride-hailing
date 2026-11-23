package com.ride_hailing.calificacion.domain;

import java.time.Instant;
import java.util.UUID;

public class Calificacion {

   private UUID id;
   private UUID viajeId;
   private UUID pasajeroId;
   private UUID conductorId;
   private Puntuacion puntuacion;
   private String comentario;
   private Instant fechaCreacion;

   public UUID getId() {
      return id;
   }

   public void setId(UUID id) {
      this.id = id;
   }

   public UUID getViajeId() {
      return viajeId;
   }

   public void setViajeId(UUID viajeId) {
      this.viajeId = viajeId;
   }

   public UUID getPasajeroId() {
      return pasajeroId;
   }

   public void setPasajeroId(UUID pasajeroId) {
      this.pasajeroId = pasajeroId;
   }

   public UUID getConductorId() {
      return conductorId;
   }

   public void setConductorId(UUID conductorId) {
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
