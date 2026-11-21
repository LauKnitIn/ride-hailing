package com.ride_hailing.calificacion.infraestructure.salida;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.RepositorioViajes;
import com.ride_hailing.calificacion.domain.ViajeNoEncontradoException;

@Component
public class ViajeAdapter implements RepositorioViajes {

    private final RestClient restClient;

    public ViajeAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${services.viajes.url:http://localhost:8080}") String viajesServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(viajesServiceUrl).build();
    }

    @Override
    public DatosViajeCalificacion buscarPorId(UUID viajeId) {
        ViajeResponse viaje;
        try {
            viaje = restClient.get()
                    .uri("/api/viajes/{viajeId}", viajeId)
                    .retrieve()
                    .body(ViajeResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ViajeNoEncontradoException(viajeId);
        }

        if (viaje == null) {
            throw new IllegalStateException("El servicio de viajes devolvio una respuesta vacia para " + viajeId);
        }

        return new DatosViajeCalificacion(
                viaje.pasajeroId(),
                viaje.conductorId(),
                "FINALIZADO".equals(viaje.estado()));
    }

    public record ViajeResponse(
            UUID pasajeroId,
            UUID conductorId,
            String estado
    ) {
    }
}
