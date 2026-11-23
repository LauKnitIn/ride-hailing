package com.ride_hailing.tarifas.infraestructure.entrada.grpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.application.DatosViajeTarifa;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.grpc.CotizarTarifaRequest;
import com.ride_hailing.tarifas.grpc.CotizarTarifaResponse;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

class TarifaGrpcServiceTest {

    private CalculoTarifaUseCase calculoTarifa;
    private TarifaGrpcService service;

    @BeforeEach
    void setUp() {
        calculoTarifa = mock(CalculoTarifaUseCase.class);
        service = new TarifaGrpcService(calculoTarifa);
    }

    @Test
    void cotizaConLosDatosRecibidosYExplicitaLaMoneda() {
        when(calculoTarifa.calcularCotizacion(new DatosViajeTarifa(10.0, 20.0)))
                .thenReturn(new Monto(21000.0));
        CapturedResponse<CotizarTarifaResponse> response = new CapturedResponse<>();

        service.cotizarTarifa(CotizarTarifaRequest.newBuilder()
                .setDistanciaKm(10.0)
                .setTiempoMinutos(20.0)
                .build(), response);

        assertThat(response.value.getMonto()).isEqualTo(21000.0);
        assertThat(response.value.getMoneda()).isEqualTo("COP");
        assertThat(response.completed).isTrue();
        ArgumentCaptor<DatosViajeTarifa> datos = ArgumentCaptor.forClass(DatosViajeTarifa.class);
        verify(calculoTarifa).calcularCotizacion(datos.capture());
        assertThat(datos.getValue()).isEqualTo(new DatosViajeTarifa(10.0, 20.0));
    }

    @Test
    void rechazaDistanciasNegativasConInvalidArgument() {
        CapturedResponse<CotizarTarifaResponse> response = new CapturedResponse<>();

        service.cotizarTarifa(CotizarTarifaRequest.newBuilder()
                .setDistanciaKm(-1.0)
                .setTiempoMinutos(20.0)
                .build(), response);

        assertThat(Status.fromThrowable(response.error).getCode()).isEqualTo(Status.Code.INVALID_ARGUMENT);
        verifyNoInteractions(calculoTarifa);
    }

    private static final class CapturedResponse<T> implements StreamObserver<T> {
        private T value;
        private Throwable error;
        private boolean completed;

        @Override
        public void onNext(T value) {
            this.value = value;
        }

        @Override
        public void onError(Throwable error) {
            this.error = error;
        }

        @Override
        public void onCompleted() {
            completed = true;
        }
    }
}
