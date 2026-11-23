package com.ride_hailing.conductores.infrastructure.salida.viajes;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.ServicioNoDisponibleException;
import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.grpc.ConductorIdRequest;
import com.ride_hailing.conductores.grpc.ViajeEnCursoResponse;
import com.ride_hailing.conductores.grpc.ViajeGrpcGrpc;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Component
public class ViajeActivoGrpc implements ViajeActivoPort {

    private final ViajeGrpcGrpc.ViajeGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public ViajeActivoGrpc(
            ViajeGrpcGrpc.ViajeGrpcBlockingStub stub,
            @Qualifier("viajesServiceCircuitBreaker") CircuitBreaker circuitBreaker) {
        this.stub = stub;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public boolean tieneViajeActivo(DriverId conductorId) {
        try {
            ViajeEnCursoResponse r = circuitBreaker.executeSupplier(() ->
                stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .conductorTieneViajeEnCurso(ConductorIdRequest.newBuilder()
                        .setConductorId(conductorId.value())
                        .build()));
            return r.getEnCurso();
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException(
                "Circuit breaker abierto consultando viajes", e);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return false;
            }
            throw new ServicioNoDisponibleException(
                "Error gRPC consultando viaje activo: " + e.getStatus().getDescription(), e);
        }
    }
}
