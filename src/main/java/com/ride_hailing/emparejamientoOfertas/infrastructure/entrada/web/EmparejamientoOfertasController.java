package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ConsultarOfertasUseCase;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;
import com.ride_hailing.viajes.dominio.Viaje;

@RestController
@RequestMapping("/api/ofertas")
public class EmparejamientoOfertasController {

    private final ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase;
    private final AceptarOfertaUseCase aceptarOfertaUseCase;
    private final RechazarOfertaUseCase rechazarOfertaUseCase;
    private final EmparejamientoWebMapper webMapper;
    private final ViajeUseCase viajeUseCase;
    private final ConsultarOfertasUseCase consultarOfertas;

    public EmparejamientoOfertasController(
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase,
            AceptarOfertaUseCase aceptarOfertaUseCase,
            RechazarOfertaUseCase rechazarOfertaUseCase,
            ViajeUseCase viajeUseCase,
            ConsultarOfertasUseCase consultarOfertas,
            EmparejamientoWebMapper webMapper) {
        this.procesarSolicitudEmparejamientoUseCase = procesarSolicitudEmparejamientoUseCase;
        this.aceptarOfertaUseCase = aceptarOfertaUseCase;
        this.rechazarOfertaUseCase = rechazarOfertaUseCase;
        this.viajeUseCase = viajeUseCase;
        this.webMapper = webMapper;
        this.consultarOfertas = consultarOfertas;
    }

    @PostMapping("/emparejamientos")
    public ResponseEntity<OfertaResponse> procesarEmparejamiento(@RequestBody SolicitudEmparejamientoRequest request) {
        Optional<Oferta> ofertaOpt = procesarSolicitudEmparejamientoUseCase.procesar(
                request.viajeId(),
                request.origenLatitud(),
                request.origenLongitud()
        );

        return ofertaOpt
                .map(oferta -> ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toResponse(oferta)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/viaje/{viajeId}")
    public List<OfertaResponse> porViaje(@PathVariable String viajeId) {
        return consultarOfertas.porViaje(viajeId).stream().map(webMapper::toResponse).toList();
    }
    
    @PostMapping("/{id}/aceptar")
    public ResponseEntity<OfertaResponse> aceptarOferta(@PathVariable("id") String id) {
        Oferta ofertaAceptada = aceptarOfertaUseCase.aceptar(id);
        return ResponseEntity.ok(webMapper.toResponse(ofertaAceptada));
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<OfertaResponse> rechazarOferta(@PathVariable("id") String id) {
        Oferta oferta = consultarOfertas.porId(id);
        Viaje viaje = viajeUseCase.consultarViaje(UUID.fromString(oferta.getViajeId().value()));

        Optional<Oferta> nuevaOferta = rechazarOfertaUseCase.rechazar(
                id, 
                viaje.getOrigen().latitud(), 
                viaje.getOrigen().longitud()
        );

        return nuevaOferta
                .map(ofertaGenerada -> ResponseEntity.ok(webMapper.toResponse(ofertaGenerada)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
