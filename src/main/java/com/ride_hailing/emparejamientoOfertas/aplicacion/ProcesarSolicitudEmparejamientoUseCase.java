package com.ride_hailing.emparejamientoOfertas.aplicacion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ride_hailing.emparejamientoOfertas.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService;
import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaId;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;


public class ProcesarSolicitudEmparejamientoUseCase {
    // 1. Declaración de la constante que faltaba
    private static final double RADIO_DEFAULT_KM = 5.0;

    private final EmparejamientoDomainService emparejamientoDomainService;
    private final OfertaRepository ofertaRepository;
    private final ConductoresPort conductoresPort;

    public ProcesarSolicitudEmparejamientoUseCase(
            EmparejamientoDomainService emparejamientoDomainService,
            OfertaRepository ofertaRepository,
            ConductoresPort conductoresPort) {
        this.emparejamientoDomainService = emparejamientoDomainService;
        this.ofertaRepository = ofertaRepository;
        this.conductoresPort = conductoresPort;
    }

    public void ejecutar(UUID viajeId, double origenLat, double origenLon) {
        // 2. Se le pasan las coordenadas y el radio al puerto
        List<CandidatoConductor> candidatos = conductoresPort.obtenerConductoresDisponibles(
                origenLat,
                origenLon,
                RADIO_DEFAULT_KM
        );

        List<UUID> rechazados = ofertaRepository.buscarPorViajeId(viajeId)
                .stream()
                .map(Oferta::getConductorId)
                .toList();

        ejecutar(viajeId, origenLat, origenLon, candidatos, rechazados, RADIO_DEFAULT_KM);
    }

    // Método principal de 6 parámetros
    public void ejecutar(
            UUID viajeId,
            double origenLat,
            double origenLon,
            List<CandidatoConductor> candidatos,
            List<UUID> rechazados,
            double radioKm) {

        CandidatoConductor candidato = emparejamientoDomainService.seleccionarSiguienteCandidato(
                origenLat, origenLon, candidatos, rechazados, radioKm
        );

        if (candidato != null) {
            Oferta nuevaOferta = OfertaFactory.crearNuevaOferta(viajeId, candidato.conductorId());
            ofertaRepository.guardar(nuevaOferta);
        }
    }
}   
