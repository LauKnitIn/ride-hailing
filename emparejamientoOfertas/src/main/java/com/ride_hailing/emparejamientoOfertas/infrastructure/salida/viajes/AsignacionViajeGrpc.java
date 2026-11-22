package com.ride_hailing.emparejamientoOfertas.infrastructure.salida.viajes;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.ReglaDeNegocioViajeException;
import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.application.ViajeNoEncontradoException;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.grpc.AsignarConductorRequest;
import com.ride_hailing.emparejamientoOfertas.grpc.OperacionResponse;
import com.ride_hailing.emparejamientoOfertas.grpc.RechazarSolicitudRequest;
import com.ride_hailing.emparejamientoOfertas.grpc.ViajeGrpcGrpc;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Component
public class AsignacionViajeGrpc implements AsignacionViajePort {

    private final ViajeGrpcGrpc.ViajeGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public AsignacionViajeGrpc(
            ViajeGrpcGrpc.ViajeGrpcBlockingStub stub,
            @Qualifier("viajesServiceCircuitBreaker") CircuitBreaker circuitBreaker) {
        this.stub = stub;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public void asignar(String viajeId, String conductorId) {
        AsignarConductorRequest req = AsignarConductorRequest.newBuilder()
                .setViajeId(viajeId)
                .setConductorId(conductorId)
                .build();
        OperacionResponse resp;
        try {
            resp = circuitBreaker.executeSupplier(() ->
                stub.withDeadlineAfter(5, TimeUnit.SECONDS).asignarConductor(req));
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException("Circuit breaker abierto en viajes", e);
        } catch (StatusRuntimeException e) {
            throw traducir(e);
        }
        if (!resp.getOk()) {
            throw new ServicioNoDisponibleException("viajes respondió ok=false en AsignarConductor");
        }
    }

    @Override
    public void rechazarPorFaltaDeConductores(String viajeId, String motivo) {
        RechazarSolicitudRequest req = RechazarSolicitudRequest.newBuilder()
                .setViajeId(viajeId)
                .setMotivo(motivo)
                .build();
        OperacionResponse resp;
        try {
            resp = circuitBreaker.executeSupplier(() ->
                stub.withDeadlineAfter(5, TimeUnit.SECONDS).rechazarPorFaltaDeConductores(req));
        } catch (CallNotPermittedException e) {
            throw new ServicioNoDisponibleException("Circuit breaker abierto en viajes", e);
        } catch (StatusRuntimeException e) {
            throw traducir(e);
        }
        if (!resp.getOk()) {
            throw new ServicioNoDisponibleException("viajes respondió ok=false en RechazarPorFaltaDeConductores");
        }
    }

    private RuntimeException traducir(StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        return switch (code) {
            case NOT_FOUND -> new ViajeNoEncontradoException(e.getStatus().getDescription());
            case FAILED_PRECONDITION -> new ReglaDeNegocioViajeException(e.getStatus().getDescription());
            case INVALID_ARGUMENT -> new IllegalArgumentException(e.getStatus().getDescription(), e);
            default -> new ServicioNoDisponibleException("Error gRPC contra viajes", e);
        };
    }
}
