package com.ride_hailing.tarifas;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.ride_hailing.tarifas.infraestructure.entrada.evento.TarifasServiceEventListener;
import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;

class TarifasServiceEventListenerTest {

    @Test
    void enviaDistanciaYDuracionAlServicioDeTarifasAlFinalizarViaje() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        TarifasServiceEventListener listener = new TarifasServiceEventListener(builder, "http://tarifas.test");
        UUID viajeId = UUID.randomUUID();

        server.expect(requestTo("http://tarifas.test/internal/viajes/" + viajeId + "/finalizado"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json("{\"distanciaKm\":3.5,\"tiempoMinutos\":1.5}"))
                .andRespond(withSuccess());

        listener.alFinalizarViaje(new ViajeFinalizadoEvent(
                viajeId, UUID.randomUUID(), 3.5, Duration.ofMillis(90_000), Instant.now()));

        server.verify();
    }

    @Test
    void notificaCancelacionesSoloCuandoRequierenCargoParcial() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        TarifasServiceEventListener listener = new TarifasServiceEventListener(builder, "http://tarifas.test");
        UUID viajeId = UUID.randomUUID();

        server.expect(requestTo("http://tarifas.test/internal/viajes/" + viajeId + "/cancelado"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        listener.alCancelarViaje(new ViajeCanceladoEvent(viajeId, UUID.randomUUID(), false, Instant.now()));
        listener.alCancelarViaje(new ViajeCanceladoEvent(viajeId, UUID.randomUUID(), true, Instant.now()));

        server.verify();
    }
}
