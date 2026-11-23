package com.ride_hailing.emparejamientoOfertas.infrastructure.entrada.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.emparejamientoOfertas.application.ServicioNoDisponibleException;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.model.ViajeId;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class ViajesSolicitadosKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ViajesSolicitadosKafkaConsumer.class);

    private final ProcesarSolicitudEmparejamientoUseCase procesar;
    private final OfertaRepositoryPort ofertaRepository;
    private final ObjectMapper objectMapper;

    public ViajesSolicitadosKafkaConsumer(
            ProcesarSolicitudEmparejamientoUseCase procesar,
            OfertaRepositoryPort ofertaRepository,
            ObjectMapper objectMapper) {
        this.procesar = procesar;
        this.ofertaRepository = ofertaRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "viajes.solicitados", groupId = "${spring.kafka.consumer.group-id}")
    public void consumir(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            String viajeId = node.get("viajeId").asString();
            double lat = node.get("latitudOrigen").asDouble();
            double lon = node.get("longitudOrigen").asDouble();

            if (ofertaRepository.existePorViajeId(new ViajeId(viajeId))) {
                log.info("viaje {} ya procesado (idempotencia), se ignora", viajeId);
                return;
            }

            procesar.procesar(viajeId, lat, lon);
            log.info("viaje {} procesado por emparejamiento", viajeId);
        } catch (ServicioNoDisponibleException e) {
            log.warn("Servicio no disponible al procesar viajes.solicitados: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Fallo procesando viajes.solicitados: {}", payload, e);
        }
    }
}
