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

}
