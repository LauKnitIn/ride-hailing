package com.ride_hailing.Conductores.application;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.Conductores.domain.model.DriverId;
import com.ride_hailing.Conductores.domain.model.TipoDocumento;
import com.ride_hailing.Conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.Conductores.domain.repository.ConductorRepository;

@Service 
public class ConductorService {

    private final ConductorRepository conductorRepository;


    public ConductorService(ConductorRepository conductorRepository){
        this.conductorRepository = conductorRepository;
    }


    public void registrarConductor(
        String numeroDocumento,
        String tipoDocumento,
        String nombre,
        LocalDate fechaNacimiento
    ){
        DocumentoIdentidad documentoIdentidad = new DocumentoIdentidad(numeroDocumento, TipoDocumento.valueOf(tipoDocumento));
        DriverId idConductor = DriverId.generar();
        Conductor driverToRegister = new Conductor(idConductor, documentoIdentidad, nombre, fechaNacimiento);
        conductorRepository.save(driverToRegister);
    }

    public void desactivarDisponibilidad(DriverId id) {
        Conductor conductor = conductorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conductor no encontrado con ID: " + id.value()));
        conductor.desactivarDisponibilidad();
        conductorRepository.save(conductor);
    }

    public void activarDisponibilidad(DriverId id) {
        Conductor conductor = conductorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conductor no encontrado con ID: " + id.value()));
        conductor.registrarDisponibilidad();
    }

    public void actualizarUbicacion(DriverId id, double latitud, double longitud) {
        Conductor conductor = conductorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conductor no encontrado con ID: " + id.value()));
        UbicacionGeografica nuevaUbicacion = new UbicacionGeografica(latitud, longitud);
        conductor.actualizarUbicacion(nuevaUbicacion);
        conductorRepository.save(conductor);
    }







}
