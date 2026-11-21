package com.ride_hailing.calificacion.infraestructure.salida;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Configuration(proxyBeanMethods = false)
@ImportGrpcClients(target = "viajes", types = ViajeGrpcBlockingStub.class)
public class ViajesGrpcConfig {

    private static final Logger log = LoggerFactory.getLogger(ViajesGrpcConfig.class);

    @Bean
    CircuitBreaker viajesServiceCircuitBreaker() {
        CircuitBreaker circuitBreaker = CircuitBreaker.of("viajes-service",
                CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .minimumNumberOfCalls(5)
                        .failureRateThreshold(50)
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        .permittedNumberOfCallsInHalfOpenState(3)
                        .ignoreException(exception -> exception instanceof StatusRuntimeException statusException
                                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND)
                        .build());
        circuitBreaker.getEventPublisher().onStateTransition(
                event -> log.warn("Circuit Breaker viajes-service: {}", event.getStateTransition()));
        return circuitBreaker;
    }
}
