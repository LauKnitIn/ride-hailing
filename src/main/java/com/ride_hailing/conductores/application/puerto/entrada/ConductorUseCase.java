package com.ride_hailing.conductores.application.puerto.entrada;



import java.time.LocalDate;
import java.util.List;

import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.TipoDocumento;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;

public interface ConductorUseCase {


    Conductor buscarPorId(DriverId id);
    List<Conductor> buscarDisponibles();
    Conductor registrar(String nombreCompleto, TipoDocumento tipoDocumento, String numeroDocumento, LocalDate fechaNacimiento);
    Conductor registrarDisponibilidad(DriverId id, UbicacionGeografica ubicacion);
    Conductor actualizarUbicacion(DriverId id, UbicacionGeografica ubicacion);
    Conductor desactivarDisponibilidad(DriverId id); 
}
