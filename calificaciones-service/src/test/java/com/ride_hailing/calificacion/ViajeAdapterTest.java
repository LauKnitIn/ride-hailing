package com.ride_hailing.calificacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.ServicioViajesNoDisponibleException;
import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;
import com.ride_hailing.calificacion.infraestructure.salida.ViajeAdapter;
import com.ride_hailing.viajes.grpc.ViajeDetalle;
import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;
import com.ride_hailing.viajes.grpc.ViajeIdRequest;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

class ViajeAdapterTest {

    @Test
    void consultaDatosDelViajeAlServicioDeViajes() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        UUID conductorId = UUID.randomUUID();
        when(stub.consultarViaje(ViajeIdRequest.newBuilder().setViajeId(viajeId.toString()).build()))
                .thenReturn(ViajeDetalle.newBuilder()
                        .setPasajeroId(pasajeroId.toString())
                        .setConductorId(conductorId.toString())
                        .setEstado("FINALIZADO")
                        .build());
        ViajeAdapter adapter = new ViajeAdapter(stub, crearCircuitBreaker());

        var viaje = adapter.buscarPorId(viajeId);

        assertThat(viaje.pasajeroId()).isEqualTo(pasajeroId);
        assertThat(viaje.conductorId()).isEqualTo(conductorId);
        assertThat(viaje.finalizado()).isTrue();
    }

    @Test
    void traduceElViajeInexistente() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenThrow(Status.NOT_FOUND.asRuntimeException());
        CircuitBreaker circuitBreaker = crearCircuitBreaker();
        ViajeAdapter adapter = new ViajeAdapter(stub, circuitBreaker);

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ViajeNoEncontradoException.class);
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(circuitBreaker.getMetrics().getNumberOfFailedCalls()).isZero();
    }

    @Test
    void traduceLaIndisponibilidadDeViajes() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        ViajeAdapter adapter = new ViajeAdapter(stub, crearCircuitBreaker());

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ServicioViajesNoDisponibleException.class);
    }

    @Test
    void abreElCircuitoYNoEnviaLlamadasMientrasViajesFalla() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        CircuitBreaker circuitBreaker = crearCircuitBreaker();
        ViajeAdapter adapter = new ViajeAdapter(stub, circuitBreaker);

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ServicioViajesNoDisponibleException.class);
        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ServicioViajesNoDisponibleException.class);
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ServicioViajesNoDisponibleException.class)
                .hasMessageContaining("circuito abierto");
        verify(stub, times(2)).consultarViaje(any());
    }

    @Test
    void cierraElCircuitoTrasUnaLlamadaExitosaEnHalfOpen() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenReturn(ViajeDetalle.newBuilder()
                .setPasajeroId(UUID.randomUUID().toString())
                .setConductorId(UUID.randomUUID().toString())
                .setEstado("FINALIZADO")
                .build());
        CircuitBreaker circuitBreaker = crearCircuitBreaker();
        circuitBreaker.transitionToOpenState();
        circuitBreaker.transitionToHalfOpenState();
        ViajeAdapter adapter = new ViajeAdapter(stub, circuitBreaker);

        DatosViajeCalificacion viaje = adapter.buscarPorId(UUID.randomUUID());

        assertThat(viaje.finalizado()).isTrue();
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    private CircuitBreaker crearCircuitBreaker() {
        return CircuitBreaker.of("test-" + UUID.randomUUID(),
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(2)
                        .minimumNumberOfCalls(2)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofHours(1))
                        .permittedNumberOfCallsInHalfOpenState(1)
                        .ignoreException(exception -> exception instanceof StatusRuntimeException statusException
                                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND)
                        .build());
    }
}
