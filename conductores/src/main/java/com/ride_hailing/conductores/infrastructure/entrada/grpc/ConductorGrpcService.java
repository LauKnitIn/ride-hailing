package com.ride_hailing.conductores.infrastructure.entrada.grpc;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.excepcion.ConductorNoEncontradoException;
import com.ride_hailing.conductores.domain.model.Conductor;
import com.ride_hailing.conductores.domain.model.DriverId;
import com.ride_hailing.conductores.grpc.ConductorDisponible;
import com.ride_hailing.conductores.grpc.ConductorGrpcGrpc;
import com.ride_hailing.conductores.grpc.ConductorRequest;
import com.ride_hailing.conductores.grpc.DisponiblesRequest;
import com.ride_hailing.conductores.grpc.DisponiblesResponse;
import com.ride_hailing.conductores.grpc.UbicacionResponse;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@Service
public class ConductorGrpcService extends ConductorGrpcGrpc.ConductorGrpcImplBase {

    private final ConductorUseCase conductorUseCase;

    public ConductorGrpcService(ConductorUseCase conductorUseCase) {
        this.conductorUseCase = conductorUseCase;
    }

    @Override
    public void ubicacionActual(ConductorRequest request, StreamObserver<UbicacionResponse> responseObserver) {
        try {
            Conductor conductor = conductorUseCase.buscarPorId(new DriverId(request.getConductorId()));
            UbicacionResponse.Builder b = UbicacionResponse.newBuilder();
            if (conductor.getUbicacionActual() != null) {
                b.setTieneUbicacion(true)
                 .setLatitud(conductor.getUbicacionActual().latitud())
                 .setLongitud(conductor.getUbicacionActual().longitud());
            } else {
                b.setTieneUbicacion(false);
            }
            responseObserver.onNext(b.build());
            responseObserver.onCompleted();
        } catch (ConductorNoEncontradoException e) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription(e.getMessage())
                .asRuntimeException());
        }
    }

    @Override
    public void conductoresDisponibles(DisponiblesRequest request,
                                       StreamObserver<DisponiblesResponse> responseObserver) {
        try {
            List<Conductor> disponibles = conductorUseCase.buscarDisponibles();
            DisponiblesResponse.Builder b = DisponiblesResponse.newBuilder();
            for (Conductor c : disponibles) {
                if (c.getUbicacionActual() == null) continue;
                b.addConductores(ConductorDisponible.newBuilder()
                    .setConductorId(c.getIdConductor().value())
                    .setLatitud(c.getUbicacionActual().latitud())
                    .setLongitud(c.getUbicacionActual().longitud())
                    .build());
            }
            responseObserver.onNext(b.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("Error consultando disponibles: " + e.getMessage())
                .asRuntimeException());
        }
    }
}
