package com.ride_hailing.emparejamientoOfertas.application;

import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.factory.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;
import com.ride_hailing.emparejamientoOfertas.domain.service.SeleccionConductorDomainService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcesarSolicitudEmparejamientoService implements ProcesarSolicitudEmparejamientoUseCase {

    private final ConductoresPort conductoresPort;
    private final OfertaRepositoryPort ofertaRepositoryPort;
    private final SeleccionConductorDomainService seleccionDomainService;
    private final OfertaFactory ofertaFactory;

    public ProcesarSolicitudEmparejamientoService(
            ConductoresPort conductoresPort,
            OfertaRepositoryPort ofertaRepositoryPort,
            SeleccionConductorDomainService seleccionDomainService,
            OfertaFactory ofertaFactory) {
        this.conductoresPort = conductoresPort;
        this.ofertaRepositoryPort = ofertaRepositoryPort;
        this.seleccionDomainService = seleccionDomainService;
        this.ofertaFactory = ofertaFactory;
    }

    @Override
    public Optional<Oferta> procesar(String viajeIdStr, double origenLatitud, double origenLongitud) {
        ViajeId viajeId = new ViajeId(viajeIdStr);

        Set<String> conductoresExcluidos = ofertaRepositoryPort.buscarPorViajeId(viajeId).stream()
                .map(Oferta::getConductorId)
                .collect(Collectors.toSet());

        List<ConductorCandidato> candidatosElegibles = conductoresPort.obtenerConductoresDisponibles().stream()
                .filter(c -> !conductoresExcluidos.contains(c.conductorId()))
                .collect(Collectors.toList());

        Optional<ConductorCandidato> seleccionado = seleccionDomainService.seleccionarMasCercano(
                candidatosElegibles, origenLatitud, origenLongitud);

        if (seleccionado.isEmpty()) {
            return Optional.empty();
        }

        Oferta nuevaOferta = ofertaFactory.crearOfertaPropuesta(viajeId, seleccionado.get().conductorId());
        Oferta guardada = ofertaRepositoryPort.guardar(nuevaOferta);
        return Optional.of(guardada);
    }
}