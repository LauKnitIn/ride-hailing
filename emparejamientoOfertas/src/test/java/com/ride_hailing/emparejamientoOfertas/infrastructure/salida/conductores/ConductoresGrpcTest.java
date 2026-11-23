package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.conductores;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;
import com.ride_hailing.emparejamientoOfertas.grpc.ConductorDisponible;
import com.ride_hailing.emparejamientoOfertas.grpc.ConductorGrpcGrpc.ConductorGrpcBlockingStub;
import com.ride_hailing.emparejamientoOfertas.grpc.DisponiblesRequest;
import com.ride_hailing.emparejamientoOfertas.grpc.DisponiblesResponse;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

class ConductoresGrpcTest {

    @Test
    void devuelveLosConductoresDelServicioGrpc() {
        ConductorGrpcBlockingStub stub = mock(ConductorGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.conductoresDisponibles(DisponiblesRequest.newBuilder().build()))
                .thenReturn(DisponiblesResponse.newBuilder()
                        .addConductores(ConductorDisponible.newBuilder()
                                .setConductorId("c-1").setLatitud(4.0).setLongitud(-73.0).build())
                        .addConductores(ConductorDisponible.newBuilder()
                                .setConductorId("c-2").setLatitud(4.1).setLongitud(-73.1).build())
                        .build());
        ConductoresGrpc adapter = new ConductoresGrpc(stub, crearCircuitBreaker());

        List<ConductorCandidato> resultado = adapter.obtenerConductoresDisponibles();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).conductorId()).isEqualTo("c-1");
        assertThat(resultado.get(0).latitud()).isEqualTo(4.0);
        assertThat(resultado.get(0).longitud()).isEqualTo(-73.0);
        assertThat(resultado.get(1).conductorId()).isEqualTo("c-2");
    }

    @Test
    void traduceLaIndisponibilidadDeConductores() {
        ConductorGrpcBlockingStub stub = mock(ConductorGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.conductoresDisponibles(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        ConductoresGrpc adapter = new ConductoresGrpc(stub, crearCircuitBreaker());

        assertThatThrownBy(adapter::obtenerConductoresDisponibles)
                .isInstanceOf(ServicioNoDisponibleException.class);
    }

    @Test
    void abreElCircuitoYNoEnviaLlamadasMientrasConductoresFalla() {
        ConductorGrpcBlockingStub stub = mock(ConductorGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.conductoresDisponibles(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        CircuitBreaker cb = crearCircuitBreaker();
        ConductoresGrpc adapter = new ConductoresGrpc(stub, cb);

        assertThatThrownBy(adapter::obtenerConductoresDisponibles)
                .isInstanceOf(ServicioNoDisponibleException.class);
        assertThatThrownBy(adapter::obtenerConductoresDisponibles)
                .isInstanceOf(ServicioNoDisponibleException.class);
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        assertThatThrownBy(adapter::obtenerConductoresDisponibles)
                .isInstanceOf(ServicioNoDisponibleException.class);
        verify(stub, times(2)).conductoresDisponibles(any());
    }

    @Test
    void cierraElCircuitoTrasLlamadaExitosaEnHalfOpen() {
        ConductorGrpcBlockingStub stub = mock(ConductorGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.conductoresDisponibles(any())).thenReturn(DisponiblesResponse.newBuilder().build());
        CircuitBreaker cb = crearCircuitBreaker();
        cb.transitionToOpenState();
        cb.transitionToHalfOpenState();
        ConductoresGrpc adapter = new ConductoresGrpc(stub, cb);

        List<ConductorCandidato> r = adapter.obtenerConductoresDisponibles();

        assertThat(r).isEmpty();
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void noCuentaNotFoundComoFalloDelCB() {
        ConductorGrpcBlockingStub stub = mock(ConductorGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.conductoresDisponibles(any())).thenThrow(Status.NOT_FOUND.asRuntimeException());
        CircuitBreaker cb = crearCircuitBreaker();
        ConductoresGrpc adapter = new ConductoresGrpc(stub, cb);

        for (int i = 0; i < 5; i++) {
            assertThatThrownBy(adapter::obtenerConductoresDisponibles)
                    .isInstanceOf(ServicioNoDisponibleException.class);
        }
        assertThat(cb.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        assertThat(cb.getMetrics().getNumberOfFailedCalls()).isZero();
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
                                && sre.getStatus().getCode() == Status.Code.NOT_FOUND)
                        .build());
    }
}
