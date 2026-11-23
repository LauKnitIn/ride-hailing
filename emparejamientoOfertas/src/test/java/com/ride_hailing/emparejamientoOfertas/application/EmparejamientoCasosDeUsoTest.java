package com.ride_hailing.emparejamientoOfertas.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.domain.exception.EstadoOfertaInvalidoException;
import com.ride_hailing.emparejamientoOfertas.domain.factory.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;
import com.ride_hailing.emparejamientoOfertas.domain.model.EstadoOferta;
import com.ride_hailing.emparejamientoOfertas.domain.model.Oferta;
import com.ride_hailing.emparejamientoOfertas.domain.service.SeleccionConductorDomainService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmparejamientoCasosDeUsoTest {

    private OfertaRepositoryFalso ofertaRepositoryFalso;
    private List<ConductorCandidato> conductoresSimulados;
    private ConductoresPort conductoresPortFake;
    private AsignacionViajePort asignacionViajePortFake;
    private List<String> viajesAsignadosLog;
    private List<String> viajesRechazadosLog;

    private ProcesarSolicitudEmparejamientoService procesarService;
    private AceptarOfertaService aceptarService;
    private RechazarOfertaService rechazarService;

    @BeforeEach
    void setUp() {
        ofertaRepositoryFalso = new OfertaRepositoryFalso();
        conductoresSimulados = new ArrayList<>();
        conductoresPortFake = () -> conductoresSimulados;
        viajesAsignadosLog = new ArrayList<>();
        viajesRechazadosLog = new ArrayList<>();
        asignacionViajePortFake = new AsignacionViajePortFake(viajesAsignadosLog, viajesRechazadosLog);

        SeleccionConductorDomainService seleccionService = new SeleccionConductorDomainService();
        OfertaFactory ofertaFactory = new OfertaFactory();

        procesarService = new ProcesarSolicitudEmparejamientoService(
                conductoresPortFake,
                ofertaRepositoryFalso,
                seleccionService,
                asignacionViajePortFake,
                ofertaFactory
        );

        aceptarService = new AceptarOfertaService(
                ofertaRepositoryFalso,
                asignacionViajePortFake
        );

        rechazarService = new RechazarOfertaService(
                ofertaRepositoryFalso,
                procesarService
        );
    }

    @Test
    void testA_EligeAlMasCercanoYAceptaAsignandoElViaje() {
        conductoresSimulados.add(new ConductorCandidato("conductor-lejano", 10.0, 10.0));
        conductoresSimulados.add(new ConductorCandidato("conductor-cercano", 4.001, -73.001));

        Optional<Oferta> ofertaOpt = procesarService.procesar("viaje-1", 4.0, -73.0);

        assertThat(ofertaOpt).isPresent();
        Oferta oferta = ofertaOpt.get();
        assertThat(oferta.getConductorId()).isEqualTo("conductor-cercano");
        assertThat(oferta.getEstado()).isEqualTo(EstadoOferta.PROPUESTA);
        assertThat(oferta.getOrigenLatitud()).isEqualTo(4.0);
        assertThat(oferta.getOrigenLongitud()).isEqualTo(-73.0);

        Oferta ofertaAceptada = aceptarService.aceptar(oferta.getId().value());

        assertThat(ofertaAceptada.getEstado()).isEqualTo(EstadoOferta.ACEPTADA);
        assertThat(viajesAsignadosLog).containsExactly("viaje-1:conductor-cercano");
    }

    @Test
    void testB_SinConductoresDevuelveVacio() {
        conductoresSimulados.clear();

        Optional<Oferta> ofertaOpt = procesarService.procesar("viaje-2", 4.0, -73.0);

        assertThat(ofertaOpt).isEmpty();
    }

    @Test
    void testC_AlRechazarProponeAlSiguienteMasCercanoYNoRepiteAlRechazado() {
        conductoresSimulados.add(new ConductorCandidato("conductor-1", 4.001, -73.001));
        conductoresSimulados.add(new ConductorCandidato("conductor-2", 4.010, -73.010));

        Optional<Oferta> oferta1Opt = procesarService.procesar("viaje-3", 4.0, -73.0);
        assertThat(oferta1Opt).isPresent();
        Oferta oferta1 = oferta1Opt.get();
        assertThat(oferta1.getConductorId()).isEqualTo("conductor-1");

        // AHORA SIN lat/lon: el origen sale de la oferta rechazada
        Optional<Oferta> oferta2Opt = rechazarService.rechazar(oferta1.getId().value());

        assertThat(oferta2Opt).isPresent();
        Oferta oferta2 = oferta2Opt.get();
        assertThat(oferta2.getConductorId()).isEqualTo("conductor-2");
        assertThat(oferta2.getId().value()).isNotEqualTo(oferta1.getId().value());
        assertThat(oferta2.getOrigenLatitud()).isEqualTo(4.0);
        assertThat(oferta2.getOrigenLongitud()).isEqualTo(-73.0);

        Oferta oferta1EnMap = ofertaRepositoryFalso.buscarPorId(oferta1.getId()).orElseThrow();
        assertThat(oferta1EnMap.getEstado()).isEqualTo(EstadoOferta.RECHAZADA);
    }

    @Test
    void testD_NoSePuedeAceptarUnaOfertaYaRechazada() {
        conductoresSimulados.add(new ConductorCandidato("conductor-1", 4.001, -73.001));

        Optional<Oferta> ofertaOpt = procesarService.procesar("viaje-4", 4.0, -73.0);
        assertThat(ofertaOpt).isPresent();
        Oferta oferta = ofertaOpt.get();

        rechazarService.rechazar(oferta.getId().value());

        assertThatThrownBy(() -> aceptarService.aceptar(oferta.getId().value()))
                .isInstanceOf(EstadoOfertaInvalidoException.class);
    }
}
