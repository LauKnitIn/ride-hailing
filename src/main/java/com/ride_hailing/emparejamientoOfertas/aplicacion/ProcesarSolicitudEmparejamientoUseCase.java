package com.ride_hailing.emparejamientoOfertas.aplicacion;

import java.util.List;
import java.util.UUID;

import com.ride_hailing.emparejamientoOfertas.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;
import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

public class ProcesarSolicitudEmparejamientoUseCase {

    private final EmparejamientoDomainService emparejamientoDomainService;
    private final OfertaRepository ofertaRepository;
    private final ConductoresPort conductoresPort;
    private final AsignacionViajePort asignacionViajePort;
    private final double radioKm;

    public ProcesarSolicitudEmparejamientoUseCase(
            EmparejamientoDomainService emparejamientoDomainService,
            OfertaRepository ofertaRepository,
            ConductoresPort conductoresPort,
            AsignacionViajePort asignacionViajePort,
            double radioKm) {
        this.emparejamientoDomainService = emparejamientoDomainService;
        this.ofertaRepository = ofertaRepository;
        this.conductoresPort = conductoresPort;
        this.asignacionViajePort = asignacionViajePort;
        this.radioKm = radioKm;
    }

    public synchronized Oferta ejecutar(UUID viajeId, double origenLat, double origenLon) {
        List<CandidatoConductor> candidatos = conductoresPort.obtenerConductoresDisponibles(
                origenLat, origenLon, radioKm);

        List<UUID> rechazados = ofertaRepository.buscarPorViajeId(viajeId)
                .stream()
                .map(Oferta::getConductorId)
                .toList();

        List<Oferta> ofertasPendientes = ofertaRepository.buscarPorEstado(EstadoOferta.PENDIENTE);

        CandidatoConductor candidato = emparejamientoDomainService.seleccionarSiguienteCandidato(
                origenLat, origenLon, candidatos, rechazados, ofertasPendientes, radioKm);

        if (candidato != null) {
            Oferta nuevaOferta = OfertaFactory.crearNuevaOferta(
                    viajeId, candidato.conductorId(), origenLat, origenLon);
            return ofertaRepository.guardar(nuevaOferta);
        } else {
            asignacionViajePort.notificarSinConductoresDisponibles(
                    viajeId, "Sin conductores disponibles en el radio de búsqueda");
            return null;
        }
    }
}