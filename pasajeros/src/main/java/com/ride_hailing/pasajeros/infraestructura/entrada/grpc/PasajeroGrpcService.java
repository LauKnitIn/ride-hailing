package com.ride_hailing.pasajeros.infraestructura.entrada.grpc;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.pasajeros.aplicacion.RepositorioPasajeros;
import com.ride_hailing.pasajeros.grpc.PasajeroGrpcGrpc;
import com.ride_hailing.pasajeros.grpc.PasajeroRequest;
import com.ride_hailing.pasajeros.grpc.PasajeroResponse;

import io.grpc.stub.StreamObserver;

@Service
public class PasajeroGrpcService extends PasajeroGrpcGrpc.PasajeroGrpcImplBase {

    private final RepositorioPasajeros repositorio;

    public PasajeroGrpcService(RepositorioPasajeros repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void existePasajero(PasajeroRequest request, StreamObserver<PasajeroResponse> responseObserver) {
        PasajeroResponse.Builder respuesta = PasajeroResponse.newBuilder().setExiste(false);
        try {
            repositorio.buscarPorId(UUID.fromString(request.getPasajeroId()))
                    .ifPresent(pasajero -> respuesta.setExiste(true).setNombre(pasajero.getNombre()));
        } catch (IllegalArgumentException idMalFormado) {
        }
        responseObserver.onNext(respuesta.build());
        responseObserver.onCompleted();
    }
}
