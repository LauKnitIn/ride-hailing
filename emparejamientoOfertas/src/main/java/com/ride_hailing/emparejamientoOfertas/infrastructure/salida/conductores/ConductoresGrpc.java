package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.conductores;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.ConductorCandidato;
import com.ride_hailing.emparejamientoOfertas.grpc.ConductorGrpcGrpc;
import com.ride_hailing.emparejamientoOfertas.grpc.DisponiblesRequest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Component
public class ConductoresGrpc implements ConductoresPort {

    private final ConductorGrpcGrpc.ConductorGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public ConductoresGrpc(
            ConductorGrpcGrpc.ConductorGrpcBlockingStub stub,
            @Qualifier("conductoresServiceCircuitBreaker") CircuitBreaker circuitBreaker) {
        this.stub = stub;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public List<ConductorCandidato> obtenerConductoresDisponibles() {
        try {
            return circuitBreaker.executeSupplier(() ->
                stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .conductoresDisponibles(DisponiblesRequest.newBuilder().build())
                    .getConductoresList().stream()
                    .map(c -> new ConductorCandidato(c.getConductorId(), c.getLatitud(), c.getLongitud()))
                    .toList());
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException(
                "Circuit breaker abierto consultando conductores", e);
        } catch (StatusRuntimeException e) {
            throw new ServicioNoDisponibleException(
                "Error gRPC consultando conductores disponibles: " + e.getStatus().getDescription(), e);
        }
    }
}
