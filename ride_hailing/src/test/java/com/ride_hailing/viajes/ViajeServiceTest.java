package com.ride_hailing.viajes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ride_hailing.viajes.aplicacion.ConductorNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.RepositorioViajes;
import com.ride_hailing.viajes.aplicacion.SolicitudRechazadaEvent;
import com.ride_hailing.viajes.aplicacion.TransicionEstadoViajeService;
import com.ride_hailing.viajes.aplicacion.ViajeAsignadoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeFactory;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeNoEncontradoException;
import com.ride_hailing.viajes.aplicacion.ViajeService;
import com.ride_hailing.viajes.aplicacion.ViajeSolicitadoEvent;
import com.ride_hailing.viajes.dominio.EstadoViaje;
import com.ride_hailing.viajes.dominio.Ubicacion;
import com.ride_hailing.viajes.dominio.Viaje;
import com.ride_hailing.viajes.infraestructura.salida.persistencia.InMemoryPublicadorEventosViaje;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViajeServiceTest {

    private RepositorioViajes viajeRepository;
    private InMemoryPublicadorEventosViaje publicadorEventos;
    private ViajeService viajeService;

    private static final double LAT_ORIGEN = 5.5353;
    private static final double LON_ORIGEN = -73.3678;
    private static final double LAT_DESTINO = 5.5450;
    private static final double LON_DESTINO = -73.3600;

    @BeforeEach
    void setUp() {
        viajeRepository = mock(RepositorioViajes.class);
        publicadorEventos = new InMemoryPublicadorEventosViaje();
        viajeService = new ViajeService(
            viajeRepository, new ViajeFactory(), new TransicionEstadoViajeService(), publicadorEventos);
    }

    @Test
    void solicitarViajeLoGuardaYPublicaElEvento() {
        UUID pasajeroId = UUID.randomUUID();

        Viaje viaje = viajeService.solicitarViaje(pasajeroId, LAT_ORIGEN, LON_ORIGEN, LAT_DESTINO, LON_DESTINO);

        assertEquals(EstadoViaje.Valor.SOLICITADO, viaje.getEstado().valor());
        verify(viajeRepository).guardar(viaje);
        assertEquals(1, publicadorEventos.eventosPublicados().size());
        assertInstanceOf(ViajeSolicitadoEvent.class, publicadorEventos.eventosPublicados().get(0));
    }

    @Test
    void asignarConductorConsultaLosViajesActivosYPublicaElEvento() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        UUID conductorId = UUID.randomUUID();
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));
        when(viajeRepository.buscarPorConductorId(conductorId)).thenReturn(List.of());

        viajeService.asignarConductor(viaje.getId(), conductorId);

        assertEquals(EstadoViaje.Valor.ASIGNADO, viaje.getEstado().valor());
        verify(viajeRepository).buscarPorConductorId(conductorId);
        verify(viajeRepository).guardar(viaje);
        assertTrue(publicadorEventos.eventosPublicados().get(0) instanceof ViajeAsignadoEvent);
    }

    @Test
    void noAsignaConductorConUnViajeEnCursoSegunElRepositorio() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        UUID conductorId = UUID.randomUUID();
        Viaje otroEnCurso = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        otroEnCurso.asignarConductor(conductorId, Instant.now());
        otroEnCurso.iniciar(Instant.now());

        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));
        when(viajeRepository.buscarPorConductorId(conductorId)).thenReturn(List.of(otroEnCurso));

        assertThrows(ConductorNoDisponibleException.class,
            () -> viajeService.asignarConductor(viaje.getId(), conductorId));
        assertTrue(publicadorEventos.eventosPublicados().isEmpty());
    }

    @Test
    void rechazarPorFaltaDeConductoresCancelaYPublicaElEvento() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));

        viajeService.rechazarPorFaltaDeConductores(viaje.getId(), "Sin conductores en el radio");

        assertEquals(EstadoViaje.Valor.CANCELADO, viaje.getEstado().valor());
        assertTrue(publicadorEventos.eventosPublicados().get(0) instanceof SolicitudRechazadaEvent);
    }

    @Test
    void consultarViajeInexistenteLanzaExcepcion() {
        UUID viajeId = UUID.randomUUID();
        when(viajeRepository.buscarPorId(viajeId)).thenReturn(Optional.empty());

        assertThrows(ViajeNoEncontradoException.class, () -> viajeService.consultarViaje(viajeId));
    }

    @Test
    void finalizarViajeCalculaDistanciaYPublicaElEvento() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        viaje.asignarConductor(UUID.randomUUID(), Instant.now());
        viaje.iniciar(Instant.now());
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));

        viajeService.finalizarViaje(viaje.getId());

        assertEquals(EstadoViaje.Valor.FINALIZADO, viaje.getEstado().valor());
        assertNotNull(viaje.getDistanciaKm());
        ViajeFinalizadoEvent evento = (ViajeFinalizadoEvent) publicadorEventos.eventosPublicados().get(0);
        assertEquals(viaje.getDistanciaKm(), evento.distanciaKm());
    }

    @Test
    void cancelarViajeDelegaEnElServicioDeDominioYPublicaElEvento() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(),
            new Ubicacion(LAT_ORIGEN, LON_ORIGEN), new Ubicacion(LAT_DESTINO, LON_DESTINO));
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            viajeService.cancelarViaje(viaje.getId(), "El pasajero cambió de planes");

        assertFalse(resultado.requiereCargoParcial());
        verify(viajeRepository).guardar(viaje);
        ViajeCanceladoEvent evento = (ViajeCanceladoEvent) publicadorEventos.eventosPublicados().get(0);
        assertFalse(evento.requiereCargoParcial());
    }
}