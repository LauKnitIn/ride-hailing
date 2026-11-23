package com.ride_hailing.viajes.infraestructura.entrada.grpc;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.ride_hailing.viajes.aplicacion.ConductorNoDisponibleException;
import com.ride_hailing.viajes.aplicacion.ViajeNoEncontradoException;
import com.ride_hailing.viajes.aplicacion.ViajeUseCase;
import com.ride_hailing.viajes.dominio.EstadoViajeInvalidoException;
import com.ride_hailing.viajes.dominio.Viaje;
import com.ride_hailing.viajes.grpc.AsignarConductorRequest;
import com.ride_hailing.viajes.grpc.ConductorIdRequest;
import com.ride_hailing.viajes.grpc.OperacionResponse;
import com.ride_hailing.viajes.grpc.RechazarSolicitudRequest;
import com.ride_hailing.viajes.grpc.ViajeDetalle;
import com.ride_hailing.viajes.grpc.ViajeEnCursoResponse;
import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc;
import com.ride_hailing.viajes.grpc.ViajeIdRequest;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/** Adaptador de entrada gRPC: expone a los otros servicios los casos de uso que antes llamaban en memoria. */
@Service
public class ViajeGrpcService extends ViajeGrpcGrpc.ViajeGrpcImplBase {

    private final ViajeUseCase viajeUseCase;

    public ViajeGrpcService(ViajeUseCase viajeUseCase) {
        this.viajeUseCase = viajeUseCase;
    }

    @Override
    public void consultarViaje(ViajeIdRequest request, StreamObserver<ViajeDetalle> responseObserver) {
        responder(responseObserver,
                () -> aDetalle(viajeUseCase.consultarViaje(UUID.fromString(request.getViajeId()))));
    }

    @Override
    public void conductorTieneViajeEnCurso(ConductorIdRequest request,
                                           StreamObserver<ViajeEnCursoResponse> responseObserver) {
        responder(responseObserver, () -> ViajeEnCursoResponse.newBuilder()
                .setEnCurso(viajeUseCase.conductorTieneViajeEnCurso(UUID.fromString(request.getConductorId())))
                .build());
    }

    @Override
    public void asignarConductor(AsignarConductorRequest request,
                                 StreamObserver<OperacionResponse> responseObserver) {
        responder(responseObserver, () -> {
            viajeUseCase.asignarConductor(
                    UUID.fromString(request.getViajeId()), UUID.fromString(request.getConductorId()));
            return OperacionResponse.newBuilder().setOk(true).build();
        });
    }

    @Override
    public void rechazarPorFaltaDeConductores(RechazarSolicitudRequest request,
                                              StreamObserver<OperacionResponse> responseObserver) {
        responder(responseObserver, () -> {
            viajeUseCase.rechazarPorFaltaDeConductores(UUID.fromString(request.getViajeId()), request.getMotivo());
            return OperacionResponse.newBuilder().setOk(true).build();
        });
    }

    private static ViajeDetalle aDetalle(Viaje viaje) {
        ViajeDetalle.Builder detalle = ViajeDetalle.newBuilder()
                .setViajeId(viaje.getId().toString())
                .setPasajeroId(viaje.getPasajeroId().toString())
                .setEstado(viaje.getEstado().valor().name());
        if (viaje.getConductorId() != null) {
            detalle.setConductorId(viaje.getConductorId().toString());
        }
        if (viaje.getDistanciaKm() != null) {
            detalle.setDistanciaKm(viaje.getDistanciaKm());
        }
        if (viaje.getDuracion() != null) {
            detalle.setDuracionMinutos(viaje.getDuracion().toMillis() / 60000.0);
        }
        return detalle.build();
    }

    /** Traduce las excepciones del núcleo a códigos de estado gRPC (tabla de errores del contrato). */
    private static <T> void responder(StreamObserver<T> responseObserver, Supplier<T> accion) {
        try {
            responseObserver.onNext(accion.get());
            responseObserver.onCompleted();
        } catch (ViajeNoEncontradoException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (EstadoViajeInvalidoException | ConductorNoDisponibleException e) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
