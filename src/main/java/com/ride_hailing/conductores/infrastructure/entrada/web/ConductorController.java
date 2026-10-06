package com.ride_hailing.conductores.infrastructure.entrada.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.infrastructure.entrada.web.dto.ConductorResponse;
import com.ride_hailing.conductores.infrastructure.entrada.web.dto.RegistrarConductorRequest;
import com.ride_hailing.conductores.infrastructure.entrada.web.dto.UbicacionRequest;
import com.ride_hailing.conductores.infrastructure.entrada.web.mapper.ConductorWebMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/conductores")
public class ConductorController {

    private final ConductorUseCase conductorUseCase;
    private final ConductorWebMapper mapper;

    public ConductorController(ConductorUseCase conductorUseCase, ConductorWebMapper mapper) {
        this.conductorUseCase = conductorUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ConductorResponse> registrar(@Valid @RequestBody RegistrarConductorRequest request) {   // ← @Valid
    Conductor conductor = conductorUseCase.registrar(
            request.nombreCompleto(),
              mapper.aTipoDocumento(request.tipoDocumento()),     
            request.numeroDocumento(),
            request.fechaNacimiento());                                   
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.aRespuesta(conductor));
    }

    @GetMapping("/disponibles")
    public List<ConductorResponse> disponibles() {
        return conductorUseCase.buscarDisponibles().stream().map(mapper::aRespuesta).toList();
    }

    @GetMapping("/{id}")
    public ConductorResponse buscarPorId(@PathVariable String id) {
    return mapper.aRespuesta(conductorUseCase.buscarPorId(new DriverId(id)));          // ← new DriverId(id)
    }

    @PutMapping("/{id}/disponibilidad")
    public ConductorResponse registrarDisponibilidad(@PathVariable String id,
                                                    @Valid @RequestBody UbicacionRequest request) {   // ← @Valid
        return mapper.aRespuesta(
                conductorUseCase.registrarDisponibilidad(new DriverId(id), mapper.aUbicacion(request)));
    }

    @PutMapping("/{id}/ubicacion")
    public ConductorResponse actualizarUbicacion(@PathVariable String id,
                                                @Valid @RequestBody UbicacionRequest request) {      // ← @Valid
        return mapper.aRespuesta(
                conductorUseCase.actualizarUbicacion(new DriverId(id), mapper.aUbicacion(request)));
    }

    @PutMapping("/{id}/desactivar")
    public ConductorResponse desactivar(@PathVariable String id) {
        return mapper.aRespuesta(conductorUseCase.desactivarDisponibilidad(new DriverId(id)));   // ← correcciones 2 y DriverId
    }
}
