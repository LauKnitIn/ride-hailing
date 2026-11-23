package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

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

import com.ride_hailing.emparejamientoOfertas.application.ReglaDeNegocioViajeException;
import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.application.ViajeNoEncontradoException;
import com.ride_hailing.emparejamientoOfertas.grpc.AsignarConductorRequest;
import com.ride_hailing.emparejamientoOfertas.grpc.OperacionResponse;
import com.ride_hailing.emparejamientoOfertas.grpc.RechazarSolicitudRequest;
import com.ride_hailing.emparejamientoOfertas.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

class AsignacionViajeGrpcTest {

    @Test
    void asignaElViajeCorrectamente() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any(AsignarConductorRequest.class)))
                .thenReturn(OperacionResponse.newBuilder().setOk(true).build());
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, crearCircuitBreaker());

        adapter.asignar("v-1", "c-1");

        verify(stub).asignarConductor(any(AsignarConductorRequest.class));
    }

    @Test
    void traduceViajeNoEncontradoEnAsignar() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any())).thenThrow(Status.NOT_FOUND.asRuntimeException());
        CircuitBreaker cb = crearCircuitBreaker();
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, cb);

        assertThatThrownBy(() -> adapter.asignar("v-1", "c-1"))
                .isInstanceOf(ViajeNoEncontradoException.class);
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(cb.getMetrics().getNumberOfFailedCalls()).isZero();
    }

    @Test
    void traduceReglaDeNegocioEnAsignar() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any())).thenThrow(Status.FAILED_PRECONDITION.asRuntimeException());
        CircuitBreaker cb = crearCircuitBreaker();
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, cb);

        assertThatThrownBy(() -> adapter.asignar("v-1", "c-1"))
                .isInstanceOf(ReglaDeNegocioViajeException.class);
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void traduceIndisponibilidadEnAsignar() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, crearCircuitBreaker());

        assertThatThrownBy(() -> adapter.asignar("v-1", "c-1"))
                .isInstanceOf(ServicioNoDisponibleException.class);
    }

    @Test
    void fallaSiViajesRespondeOkFalse() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any()))
                .thenReturn(OperacionResponse.newBuilder().setOk(false).build());
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, crearCircuitBreaker());

        assertThatThrownBy(() -> adapter.asignar("v-1", "c-1"))
                .isInstanceOf(ServicioNoDisponibleException.class)
                .hasMessageContaining("ok=false");
    }

    @Test
    void rechazaPorFaltaDeConductoresCorrectamente() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.rechazarPorFaltaDeConductores(any(RechazarSolicitudRequest.class)))
                .thenReturn(OperacionResponse.newBuilder().setOk(true).build());
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, crearCircuitBreaker());

        adapter.rechazarPorFaltaDeConductores("v-1", "sin conductores");

        verify(stub).rechazarPorFaltaDeConductores(any(RechazarSolicitudRequest.class));
    }

    @Test
    void abreElCircuitoYNoEnviaLlamadasMientrasViajesFalla() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.asignarConductor(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        CircuitBreaker cb = crearCircuitBreaker();
        AsignacionViajeGrpc adapter = new AsignacionViajeGrpc(stub, cb);

        assertThatThrownBy(() -> adapter.asignar("v", "c"))
                .isInstanceOf(ServicioNoDisponibleException.class);
        assertThatThrownBy(() -> adapter.asignar("v", "c"))
                .isInstanceOf(ServicioNoDisponibleException.class);
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        assertThatThrownBy(() -> adapter.asignar("v", "c"))
                .isInstanceOf(ServicioNoDisponibleException.class);
        verify(stub, times(2)).asignarConductor(any());
    }

    private CircuitBreaker crearCircuitBreaker() {
        return CircuitBreaker.of("test-" + UUID.randomUUID(),
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(2)
                        .minimumNumberOfCalls(2)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofHours(1))
                        .permittedNumberOfCallsInHalfOpenState(1)
                        .ignoreException(e -> e instanceof StatusRuntimeException sre
                                && (sre.getStatus().getCode() == Status.Code.NOT_FOUND
                                    || sre.getStatus().getCode() == Status.Code.FAILED_PRECONDITION))
                        .build());
    }
}
