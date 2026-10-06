package com.ride_hailing.conductores.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.application.puerto.salida.ConductorRepositoryPort;
import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.excepcion.ConductorNoEncontradoException;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DocumentoIdentidad;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.domain.model.TipoDocumento;
import com.ride_hailing.conductores.domain.model.UbicacionGeografica;
import com.ride_hailing.conductores.domain.service.ConductorDomainService;


public class ConductorService implements ConductorUseCase {

    private final ConductorRepositoryPort conductorRepository;
    private final ViajeActivoPort viajeActivoPort;
    private final ConductorFactory conductorFactory;
    private final ConductorDomainService conductorDomainService;

    public ConductorService(ConductorRepositoryPort conductorRepository,
                            ViajeActivoPort viajeActivoPort,
                            ConductorFactory conductorFactory,
                            ConductorDomainService conductorDomainService) {
        this.conductorRepository = conductorRepository;
        this.viajeActivoPort = viajeActivoPort;
        this.conductorFactory = conductorFactory;
        this.conductorDomainService = conductorDomainService;
    }


    @Override
    public Conductor buscarPorId(DriverId id) {
        return cargar(id);
    }


    @Override
    public Conductor registrar(String nombreCompleto, TipoDocumento tipoDocumento, String numeroDocumento,
                            LocalDate fechaNacimiento) {
        Conductor conductor = conductorFactory.crear(
                nombreCompleto, new DocumentoIdentidad(numeroDocumento, tipoDocumento), fechaNacimiento);
        return conductorRepository.guardar(conductor);
    }

    @Override
    public Conductor registrarDisponibilidad(DriverId id, UbicacionGeografica ubicacion) {
        Conductor conductor = cargar(id);
        conductor.actualizarUbicacion(ubicacion);   
        conductor.registrarDisponibilidad();
        return conductorRepository.guardar(conductor);
    }

    @Override
    public Conductor desactivarDisponibilidad(DriverId id) {
        Conductor conductor = cargar(id);
        conductorDomainService.validarDesactivacion(conductor, viajeActivoPort.tieneViajeActivo(id));
        conductor.desactivarDisponibilidad();
        return conductorRepository.guardar(conductor);
    }

    @Override
    public Conductor actualizarUbicacion(DriverId id, UbicacionGeografica ubicacion) {
        Conductor conductor = cargar(id);
        conductor.actualizarUbicacion(ubicacion);
        return conductorRepository.guardar(conductor);
    }


    private Conductor cargar(DriverId id) {
        return conductorRepository.buscarPorId(id).orElseThrow(() -> new ConductorNoEncontradoException(id));
    }


    @Override
    public List<Conductor> buscarDisponibles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarDisponibles'");
    }
}
