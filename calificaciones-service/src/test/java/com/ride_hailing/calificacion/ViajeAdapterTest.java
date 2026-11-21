package com.ride_hailing.calificacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.ride_hailing.calificacion.application.ServicioViajesNoDisponibleException;
import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;
import com.ride_hailing.calificacion.infraestructure.salida.ViajeAdapter;
import com.ride_hailing.viajes.grpc.ViajeDetalle;
import com.ride_hailing.viajes.grpc.ViajeGrpcGrpc.ViajeGrpcBlockingStub;
import com.ride_hailing.viajes.grpc.ViajeIdRequest;

import io.grpc.Status;

class ViajeAdapterTest {

    @Test
    void consultaDatosDelViajeAlServicioDeViajes() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        UUID conductorId = UUID.randomUUID();
        when(stub.consultarViaje(ViajeIdRequest.newBuilder().setViajeId(viajeId.toString()).build()))
                .thenReturn(ViajeDetalle.newBuilder()
                        .setPasajeroId(pasajeroId.toString())
                        .setConductorId(conductorId.toString())
                        .setEstado("FINALIZADO")
                        .build());
        ViajeAdapter adapter = new ViajeAdapter(stub);

        var viaje = adapter.buscarPorId(viajeId);

        assertThat(viaje.pasajeroId()).isEqualTo(pasajeroId);
        assertThat(viaje.conductorId()).isEqualTo(conductorId);
        assertThat(viaje.finalizado()).isTrue();
    }

    @Test
    void traduceElViajeInexistente() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenThrow(Status.NOT_FOUND.asRuntimeException());
        ViajeAdapter adapter = new ViajeAdapter(stub);

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ViajeNoEncontradoException.class);
    }

    @Test
    void traduceLaIndisponibilidadDeViajes() {
        ViajeGrpcBlockingStub stub = mock(ViajeGrpcBlockingStub.class);
        when(stub.withDeadlineAfter(5, TimeUnit.SECONDS)).thenReturn(stub);
        when(stub.consultarViaje(any())).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        ViajeAdapter adapter = new ViajeAdapter(stub);

        assertThatThrownBy(() -> adapter.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(ServicioViajesNoDisponibleException.class);
    }
}
