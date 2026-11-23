package com.ride_hailing.viajes.infraestructura.salida.conductores;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.ServicioNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.UbicacionConductorPort;
import com.ride_hailing.viajes.dominio.Ubicacion;
import com.ride_hailing.viajes.grpc.ConductorGrpcGrpc.ConductorGrpcBlockingStub;
import com.ride_hailing.viajes.grpc.ConductorRequest;
import com.ride_hailing.viajes.grpc.UbicacionResponse;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/** Adaptador de salida: pide la ubicación actual a conductores-service por gRPC, con Circuit Breaker. */
@Component
public class UbicacionConductorGrpc implements UbicacionConductorPort {

    private static final Logger log = LoggerFactory.getLogger(UbicacionConductorGrpc.class);

    private final ConductorGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public UbicacionConductorGrpc(ConductorGrpcBlockingStub stub) {
        this.stub = stub;
        this.circuitBreaker = CircuitBreaker.of("conductores-service",
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        // "ese conductor no existe" es una respuesta, no una caída: no abre el circuito
                        .ignoreException(e -> e instanceof StatusRuntimeException s
                                && s.getStatus().getCode() == Status.Code.NOT_FOUND)
                        .build());
        this.circuitBreaker.getEventPublisher().onStateTransition(
                e -> log.warn("Circuit Breaker conductores-service: {}", e.getStateTransition()));
    }

    @Override
    public Optional<Ubicacion> ubicacionActual(UUID conductorId) {
        try {
            UbicacionResponse respuesta = circuitBreaker.executeSupplier(() ->
                    stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                            .ubicacionActual(ConductorRequest.newBuilder()
                                    .setConductorId(conductorId.toString())
                                    .build()));
            if (!respuesta.getTieneUbicacion()) {
                return Optional.empty();
            }
            return Optional.of(new Ubicacion(respuesta.getLatitud(), respuesta.getLongitud()));
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException("conductores-service no está disponible (circuito abierto)", e);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return Optional.empty();
            }
            throw new ServicioNoDisponibleException(
                    "No se pudo contactar a conductores-service: " + e.getStatus().getCode(), e);
        }
    }
}