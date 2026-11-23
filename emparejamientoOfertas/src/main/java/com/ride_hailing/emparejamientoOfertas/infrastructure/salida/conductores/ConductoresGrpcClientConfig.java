package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.conductores;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.emparejamientoOfertas.grpc.ConductorGrpcGrpc;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Configuration
@ImportGrpcClients(target = "conductores", types = ConductorGrpcGrpc.ConductorGrpcBlockingStub.class)
public class ConductoresGrpcClientConfig {

    @Bean
    @Qualifier("conductoresServiceCircuitBreaker")
    public CircuitBreaker conductoresServiceCircuitBreaker() {
        return CircuitBreaker.of("conductores-service", CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(3)
                .ignoreException(e -> e instanceof StatusRuntimeException sre
                        && sre.getStatus().getCode() == Status.Code.NOT_FOUND)
                .build());
    }
}
