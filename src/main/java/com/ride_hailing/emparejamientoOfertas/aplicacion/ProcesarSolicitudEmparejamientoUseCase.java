package com.ride_hailing.emparejamientoOfertas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService;
import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;


public class ProcesarSolicitudEmparejamientoUseCase {
   private final OfertaRepository ofertaRepository;
    private final ConductoresPort conductoresPort;
    private final AsignacionViajePort asignacionViajePort;
    private final EmparejamientoDomainService matchingDomainService;
    private final double radioMaximoKm;


    public ProcesarSolicitudEmparejamientoUseCase(
            OfertaRepository ofertaRepository,
            ConductoresPort conductoresPort,
            AsignacionViajePort asignacionViajePort,
            EmparejamientoDomainService matchingDomainService,
            double radioMaximoKm) {
        this.ofertaRepository = ofertaRepository;
        this.conductoresPort = conductoresPort;
        this.asignacionViajePort = asignacionViajePort;
        this.matchingDomainService = matchingDomainService; // Se asigna al atributo de instancia
        this.radioMaximoKm = radioMaximoKm;
    }

    public void ejecutar(UUID viajeId, double origenLat, double origenLon) {
        List<Oferta> ofertasExistentes = ofertaRepository.buscarPorViajeId(viajeId);
        List<UUID> conductoresQueRechazaron = ofertasExistentes.stream()
                .filter(o -> o.getEstado() == EstadoOferta.RECHAZADA)
                .map(Oferta::getConductorId)
                .toList();

        List<CandidatoConductor> disponibles = conductoresPort.obtenerConductoresDisponibles(origenLat, origenLon, radioMaximoKm);

        CandidatoConductor candidato = this.matchingDomainService.seleccionarSiguienteCandidato(
                origenLat, origenLon, disponibles, conductoresQueRechazaron, radioMaximoKm);

        if (candidato == null) {
            asignacionViajePort.notificarSinConductoresDisponibles(viajeId, "No hay conductores disponibles en el radio de búsqueda");
            return;
        }

        Oferta nuevaOferta = new Oferta(OfertaId.generar(), viajeId, candidato.conductorId());
        ofertaRepository.guardar(nuevaOferta);
    }
}   
