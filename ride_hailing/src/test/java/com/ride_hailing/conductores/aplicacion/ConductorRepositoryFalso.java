package com.ride_hailing.conductores.aplicacion;



import java.util.*;

import com.ride_hailing.conductores.application.puerto.salida.ConductorRepositoryPort;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.EstadoDisponibilidad;


class ConductorRepositoryFalso implements ConductorRepositoryPort {

    private final Map<String, Conductor> almacen = new LinkedHashMap<>();

    @Override
    public Optional<Conductor> buscarPorId(DriverId id) {
        return Optional.ofNullable(almacen.get(String.valueOf(id.value())));
    }

    @Override
    public List<Conductor> buscarPorEstado(EstadoDisponibilidad estado) {
        return almacen.values().stream().filter(c -> c.getDisponibilidad() == estado).toList();
    }

    @Override
    public boolean existeDocumento(DocumentoIdentidad documento) {
        return almacen.values().stream().anyMatch(c -> c.getDocumentoIdentidad().equals(documento));
    }

    @Override
    public Conductor guardar(Conductor conductor) {
        almacen.put(String.valueOf(conductor.getIdConductor().value()), conductor);
        return conductor;
    }

  
}
