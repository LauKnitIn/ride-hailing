package com.ride_hailing.emparejamientoOfertas.infrastructure.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.emparejamientoOfertas.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService.CandidatoConductor;
import com.ride_hailing.emparejamientoOfertas.dominio.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.dominio.Oferta;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@Service
public class ProcesarSolicitudEmparejamientoUseCase {
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

    public void ejecutar(
            UUID viajeId,
            double origenLat,
            double origenLon,
            List<CandidatoConductor> candidatos,
            List<UUID> rechazados,
            double radioKm) {

        List<Oferta> ofertasPendientes = ofertaRepository.buscarPorEstado(EstadoOferta.PENDIENTE);

        CandidatoConductor candidato = emparejamientoDomainService.seleccionarSiguienteCandidato(
                origenLat, 
                origenLon, 
                candidatos, 
                rechazados, 
                ofertasPendientes, 
                radioKm
        );

        if (candidato != null) {
            Oferta nuevaOferta = OfertaFactory.crearNuevaOferta(
                    viajeId, 
                    candidato.conductorId(), 
                    origenLat, 
                    origenLon
            );
            ofertaRepository.guardar(nuevaOferta);
        }
    }
}