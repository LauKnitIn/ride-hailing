package com.ride_hailing.tarifas.infraestructure.entrada.grpc;

import org.springframework.stereotype.Service;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.application.DatosViajeTarifa;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.grpc.CotizarTarifaRequest;
import com.ride_hailing.tarifas.grpc.CotizarTarifaResponse;
import com.ride_hailing.tarifas.grpc.TarifaGrpcGrpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@Service
public class TarifaGrpcService extends TarifaGrpcGrpc.TarifaGrpcImplBase {

    private static final String MONEDA = "COP";

    private final CalculoTarifaUseCase calculoTarifa;

    public TarifaGrpcService(CalculoTarifaUseCase calculoTarifa) {
        this.calculoTarifa = calculoTarifa;
    }

    @Override
    public void cotizarTarifa(CotizarTarifaRequest request,
                              StreamObserver<CotizarTarifaResponse> responseObserver) {
        if (!Double.isFinite(request.getDistanciaKm()) || request.getDistanciaKm() < 0
                || !Double.isFinite(request.getTiempoMinutos()) || request.getTiempoMinutos() < 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("La distancia y la duración deben ser valores finitos no negativos.")
                    .asRuntimeException());
            return;
        }

        Monto monto = calculoTarifa.calcularCotizacion(
                new DatosViajeTarifa(request.getDistanciaKm(), request.getTiempoMinutos()));
        responseObserver.onNext(CotizarTarifaResponse.newBuilder()
                .setMonto(monto.valor())
                .setMoneda(MONEDA)
                .build());
        responseObserver.onCompleted();
    }
}
