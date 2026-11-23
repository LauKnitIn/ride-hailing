package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

import com.ride_hailing.emparejamientoOfertas.grpc.ViajeGrpcGrpc;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Configuration
@ImportGrpcClients(target = "viajes", types = ViajeGrpcGrpc.ViajeGrpcBlockingStub.class)
public class ViajesGrpcClientConfig {

    @Bean
    @Qualifier("viajesServiceCircuitBreaker")
    public CircuitBreaker viajesServiceCircuitBreaker() {
        return CircuitBreaker.of("viajes-service", CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(3)
                .ignoreException(e -> e instanceof StatusRuntimeException sre
                        && (sre.getStatus().getCode() == Status.Code.NOT_FOUND
                            || sre.getStatus().getCode() == Status.Code.FAILED_PRECONDITION))
                .build());
    }
}
