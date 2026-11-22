package com.ride_hailing.conductores.infrastructure.entrada.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.domain.excepcion.ConductorNoEncontradoException;
import com.ride_hailing.conductores.domain.model.DriverId;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class ViajesKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ViajesKafkaConsumer.class);

    private final ConductorUseCase conductorUseCase;
    private final ObjectMapper objectMapper;

    public ViajesKafkaConsumer(ConductorUseCase conductorUseCase, ObjectMapper objectMapper) {
        this.conductorUseCase = conductorUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "viajes.asignados", groupId = "${spring.kafka.consumer.group-id}")
    public void onViajeAsignado(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode cNode = node.get("conductorId");
            if (cNode == null || cNode.isNull()) {
                log.warn("viajes.asignados sin conductorId, se ignora: {}", payload);
                return;
            }
            String conductorId = cNode.asString();
            if (conductorId.isBlank()) {
                log.warn("viajes.asignados con conductorId vacío, se ignora");
                return;
            }
            conductorUseCase.marcarEnViaje(new DriverId(conductorId));
            log.info("Conductor {} marcado EN_VIAJE", conductorId);
        } catch (ConductorNoEncontradoException e) {
            log.warn("Conductor no encontrado al marcar en viaje: {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Conductor ya no está disponible (idempotencia): {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "viajes.finalizados", groupId = "${spring.kafka.consumer.group-id}")
    public void onViajeFinalizado(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode cNode = node.get("conductorId");
            if (cNode == null || cNode.isNull()) {
                log.warn("viajes.finalizados sin conductorId, se ignora: {}", payload);
                return;
            }
            String conductorId = cNode.asString();
            conductorUseCase.liberarDeViaje(new DriverId(conductorId));
            log.info("Conductor {} liberado (viaje finalizado)", conductorId);
        } catch (ConductorNoEncontradoException e) {
            log.warn("Conductor no encontrado al liberar (finalizado): {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Conductor en estado inesperado al liberar (finalizado): {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "viajes.cancelados", groupId = "${spring.kafka.consumer.group-id}")
    public void onViajeCancelado(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            JsonNode cNode = node.get("conductorId");
            if (cNode == null || cNode.isNull()) {
                log.info("viajes.cancelados sin conductorId (sin asignación previa), se ignora");
                return;
            }
            String conductorId = cNode.asString();
            if (conductorId.isBlank()) {
                log.info("viajes.cancelados con conductorId vacío, se ignora");
                return;
            }
            conductorUseCase.liberarDeViaje(new DriverId(conductorId));
            log.info("Conductor {} liberado (viaje cancelado)", conductorId);
        } catch (ConductorNoEncontradoException e) {
            log.warn("Conductor no encontrado al liberar (cancelado): {}", e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Conductor en estado inesperado al liberar (cancelado): {}", e.getMessage());
        }
    }
}
