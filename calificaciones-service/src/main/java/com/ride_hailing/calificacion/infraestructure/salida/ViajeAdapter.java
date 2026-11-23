package com.ride_hailing.calificacion.infraestructure.salida;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.RepositorioViajes;
import com.ride_hailing.calificacion.application.ServicioViajesNoDisponibleException;
import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;
import com.ride_hailing.viajes.grpc.ViajeDetalle;
import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;
import com.ride_hailing.viajes.grpc.ViajeIdRequest;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Component
public class ViajeAdapter implements RepositorioViajes {

    private static final long DEADLINE_SECONDS = 5;

    private final ViajeGrpcBlockingStub stub;
    private final CircuitBreaker circuitBreaker;

    public ViajeAdapter(
            ViajeGrpcBlockingStub stub,
            @Qualifier("viajesServiceCircuitBreaker") CircuitBreaker circuitBreaker) {
        this.stub = stub;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public DatosViajeCalificacion buscarPorId(UUID viajeId) {
        ViajeDetalle viaje;
        try {
            viaje = circuitBreaker.executeSupplier(() ->
                    stub.withDeadlineAfter(DEADLINE_SECONDS, TimeUnit.SECONDS)
                            .consultarViaje(ViajeIdRequest.newBuilder().setViajeId(viajeId.toString()).build()));
        } catch (CallNotPermittedException exception) {
            throw new ServicioViajesNoDisponibleException(
                    "viajes-service no está disponible (circuito abierto)", exception);
        } catch (StatusRuntimeException exception) {
            if (exception.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ViajeNoEncontradoException(viajeId);
            }
            throw new ServicioViajesNoDisponibleException(
                    "No se pudo consultar el viaje en viajes-service: " + exception.getStatus().getCode(),
                    exception);
        }

        return new DatosViajeCalificacion(
                UUID.fromString(viaje.getPasajeroId()),
                viaje.getConductorId().isBlank() ? null : UUID.fromString(viaje.getConductorId()),
                "FINALIZADO".equals(viaje.getEstado()));
    }
}
