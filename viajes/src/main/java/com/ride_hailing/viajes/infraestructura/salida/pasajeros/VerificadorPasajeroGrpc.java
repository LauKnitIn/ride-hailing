package com.ride_hailing.viajes.infraestructura.salida.pasajeros;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.ServicioNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.VerificadorPasajero;
import com.ride_hailing.viajes.grpc.PasajeroGrpcGrpc.PasajeroGrpcBlockingStub;
import com.ride_hailing.viajes.grpc.PasajeroRequest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.StatusRuntimeException;

@Component
public class VerificadorPasajeroGrpc implements VerificadorPasajero {

    private static final Logger log = LoggerFactory.getLogger(VerificadorPasajeroGrpc.class);

    private final PasajeroGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public VerificadorPasajeroGrpc(PasajeroGrpcBlockingStub stub) {
        this.stub = stub;
        this.circuitBreaker = CircuitBreaker.of("pasajeros-service",
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .build());
        this.circuitBreaker.getEventPublisher().onStateTransition(
                e -> log.warn("Circuit Breaker pasajeros-service: {}", e.getStateTransition()));
    }

    @Override
    public boolean existe(UUID pasajeroId) {
        try {
            return circuitBreaker.executeSupplier(() -> {
                var request = PasajeroRequest.newBuilder()
                        .setPasajeroId(pasajeroId.toString())
                        .build();
                return stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .existePasajero(request)
                        .getExiste();
            });
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException("pasajeros-service no está disponible (circuito abierto)", e);
        } catch (StatusRuntimeException e) {
            throw new ServicioNoDisponibleException(
                    "No se pudo contactar a pasajeros-service: " + e.getStatus().getCode(), e);
        }
    }
}
