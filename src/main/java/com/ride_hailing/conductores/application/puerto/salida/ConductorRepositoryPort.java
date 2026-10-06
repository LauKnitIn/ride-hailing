package com.ride_hailing.conductores.application.puerto.salida;



import java.util.List;
import java.util.Optional;

import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;

public interface ConductorRepositoryPort {

     Optional<Conductor> buscarPorId(DriverId id);

    List<Conductor> buscarPorEstado(EstadoDisponibilidad estado);

    boolean existeDocumento(DocumentoIdentidad documento);

    Conductor guardar(Conductor conductor);
}
