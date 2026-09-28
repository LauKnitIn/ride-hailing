package com.ride_hailing.viajes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViajeServiceTest {

    private ViajeRepository viajeRepository;
    private ViajeService viajeService;

    @BeforeEach
    void setUp() {
        viajeRepository = mock(ViajeRepository.class);
        viajeService = new ViajeService(viajeRepository, new ViajeFactory(), new TransicionEstadoViajeService());
    }

    @Test
    void solicitarViajeLoGuardaEnElRepositorio() {
        UUID pasajeroId = UUID.randomUUID();

        Viaje viaje = viajeService.solicitarViaje(pasajeroId, "Calle 1", "Calle 100");

        assertEquals(EstadoViaje.Valor.SOLICITADO, viaje.getEstado().valor());
        verify(viajeRepository).guardar(viaje);
    }

    @Test
    void asignarConductorConsultaLosViajesActivosDelConductor() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        UUID conductorId = UUID.randomUUID();
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));
        when(viajeRepository.buscarPorConductorId(conductorId)).thenReturn(List.of());

        viajeService.asignarConductor(viaje.getId(), conductorId);

        assertEquals(EstadoViaje.Valor.ASIGNADO, viaje.getEstado().valor());
        verify(viajeRepository).buscarPorConductorId(conductorId);
        verify(viajeRepository).guardar(viaje);
    }

    @Test
    void noAsignaConductorConUnViajeEnCursoSegunElRepositorio() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        UUID conductorId = UUID.randomUUID();
        Viaje otroEnCurso = new ViajeFactory().solicitar(UUID.randomUUID(), "Carrera 5", "Carrera 50");
        otroEnCurso.asignarConductor(conductorId, java.time.LocalDateTime.now());
        otroEnCurso.iniciar();

        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));
        when(viajeRepository.buscarPorConductorId(conductorId)).thenReturn(List.of(otroEnCurso));

        assertThrows(ConductorNoDisponibleException.class,
            () -> viajeService.asignarConductor(viaje.getId(), conductorId));
    }

    @Test
    void consultarViajeInexistenteLanzaExcepcion() {
        UUID viajeId = UUID.randomUUID();
        when(viajeRepository.buscarPorId(viajeId)).thenReturn(Optional.empty());

        assertThrows(ViajeNoEncontradoException.class, () -> viajeService.consultarViaje(viajeId));
    }

    @Test
    void cancelarViajeDelegaEnElServicioDeDominioYGuarda() {
        Viaje viaje = new ViajeFactory().solicitar(UUID.randomUUID(), "Calle 1", "Calle 100");
        when(viajeRepository.buscarPorId(viaje.getId())).thenReturn(Optional.of(viaje));

        TransicionEstadoViajeService.ResultadoCancelacion resultado =
            viajeService.cancelarViaje(viaje.getId(), "El pasajero cambió de planes");

        assertFalse(resultado.requiereCargoParcial());
        verify(viajeRepository).guardar(viaje);
    }
}
