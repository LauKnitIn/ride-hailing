package com.ride_hailing.tarifas.infraestructure.entrada.evento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;

@Component
@ConditionalOnProperty(name = "services.tarifas.enabled", havingValue = "true", matchIfMissing = true)
public class TarifasServiceEventListener {

    private final RestClient restClient;

    public TarifasServiceEventListener(
            RestClient.Builder restClientBuilder,
            @Value("${services.tarifas.url:http://localhost:8081}") String tarifasServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(tarifasServiceUrl).build();
    }

    @EventListener
    public void alFinalizarViaje(ViajeFinalizadoEvent evento) {
        double tiempoMinutos = evento.duracion().toMillis() / 60000.0;
        restClient.post()
                .uri("/internal/viajes/{viajeId}/finalizado", evento.viajeId())
                .body(new ViajeFinalizadoRequest(evento.distanciaKm(), tiempoMinutos))
                .retrieve()
                .toBodilessEntity();
    }

    @EventListener
    public void alCancelarViaje(ViajeCanceladoEvent evento) {
        if (evento.requiereCargoParcial()) {
            restClient.post()
                    .uri("/internal/viajes/{viajeId}/cancelado", evento.viajeId())
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    public record ViajeFinalizadoRequest(double distanciaKm, double tiempoMinutos) {
    }
}
