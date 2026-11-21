package com.ride_hailing.calificacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;
import com.ride_hailing.calificacion.infraestructure.salida.ViajeAdapter;

class ViajeAdapterTest {

    @Test
    void consultaDatosDelViajeAlServicioDeViajes() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        ViajeAdapter adapter = new ViajeAdapter(builder, "http://viajes.test");
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        UUID conductorId = UUID.randomUUID();

        server.expect(requestTo("http://viajes.test/api/viajes/" + viajeId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {"pasajeroId":"%s","conductorId":"%s","estado":"FINALIZADO"}
                        """.formatted(pasajeroId, conductorId), MediaType.APPLICATION_JSON));

        var viaje = adapter.buscarPorId(viajeId);

        assertThat(viaje.pasajeroId()).isEqualTo(pasajeroId);
        assertThat(viaje.conductorId()).isEqualTo(conductorId);
        assertThat(viaje.finalizado()).isTrue();
        server.verify();
    }

    @Test
    void traduceElViajeInexistente() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        ViajeAdapter adapter = new ViajeAdapter(builder, "http://viajes.test");
        UUID viajeId = UUID.randomUUID();

        server.expect(requestTo("http://viajes.test/api/viajes/" + viajeId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound());

        assertThatThrownBy(() -> adapter.buscarPorId(viajeId))
                .isInstanceOf(ViajeNoEncontradoException.class);
        server.verify();
    }
}
