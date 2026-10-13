package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ConsultarOfertasUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;
import com.ride_hailing.viajes.dominio.Viaje;

/** HU-04: el conductor acepta o rechaza la oferta que se le asignó. */
@RestController
@RequestMapping("/api/ofertas")
public class OfertaController {

    private final AceptarOfertaUseCase aceptarOferta;
    private final RechazarOfertaUseCase rechazarOferta;
    private final ConsultarOfertasUseCase consultarOfertas;
    private final ViajeUseCase viajeUseCase;

    public OfertaController(AceptarOfertaUseCase aceptarOferta,
                            RechazarOfertaUseCase rechazarOferta,
                            ConsultarOfertasUseCase consultarOfertas,
                            ViajeUseCase viajeUseCase) {
        this.aceptarOferta = aceptarOferta;
        this.rechazarOferta = rechazarOferta;
        this.consultarOfertas = consultarOfertas;
        this.viajeUseCase = viajeUseCase;
    }

    @GetMapping("/viaje/{viajeId}")
    public List<OfertaResponse> porViaje(@PathVariable String viajeId) {
        return consultarOfertas.porViaje(viajeId).stream().map(OfertaResponse::desde).toList();
    }

    @PostMapping("/{id}/aceptar")
    public OfertaResponse aceptar(@PathVariable String id) {
        return OfertaResponse.desde(aceptarOferta.aceptar(id));
    }

    /** Rechaza la oferta y reofrece el viaje al siguiente conductor más cercano, si lo hay. */
    @PostMapping("/{id}/rechazar")
    public ResponseEntity<OfertaResponse> rechazar(@PathVariable String id) {
        Oferta oferta = consultarOfertas.porId(id);
        Viaje viaje = viajeUseCase.consultarViaje(UUID.fromString(oferta.getViajeId().value()));

        return rechazarOferta.rechazar(id, viaje.getOrigen().latitud(), viaje.getOrigen().longitud())
                .map(nueva -> ResponseEntity.ok(OfertaResponse.desde(nueva)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
