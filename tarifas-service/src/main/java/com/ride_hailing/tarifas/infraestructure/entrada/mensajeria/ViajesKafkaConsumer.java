package com.ride_hailing.tarifas.infraestructure.entrada.mensajeria;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.application.DatosViajeTarifa;

import tools.jackson.databind.ObjectMapper;

@Component
public class ViajesKafkaConsumer {

    private final ObjectMapper objectMapper;
    private final CalculoTarifaUseCase calculoTarifa;

    public ViajesKafkaConsumer(ObjectMapper objectMapper, CalculoTarifaUseCase calculoTarifa) {
        this.objectMapper = objectMapper;
        this.calculoTarifa = calculoTarifa;
    }

    @KafkaListener(topics = "viajes.finalizados", groupId = "${spring.kafka.consumer.group-id}")
    public void alFinalizarViaje(String payload) {
        ViajeFinalizadoMessage evento = objectMapper.readValue(payload, ViajeFinalizadoMessage.class);
        if (evento == null) {
            throw new IllegalArgumentException("El evento de finalización no puede estar vacío.");
        }
        validarMedidas(evento.distanciaKm(), evento.duracionMinutos());
        calculoTarifa.registrarViajeFinalizado(
                parsearViajeId(evento.viajeId()),
                new DatosViajeTarifa(evento.distanciaKm(), evento.duracionMinutos()));
    }

    @KafkaListener(topics = "viajes.cancelados", groupId = "${spring.kafka.consumer.group-id}")
    public void alCancelarViaje(String payload) {
        ViajeCanceladoMessage evento = objectMapper.readValue(payload, ViajeCanceladoMessage.class);
        if (evento == null) {
            throw new IllegalArgumentException("El evento de cancelación no puede estar vacío.");
        }
        if (evento.requiereCargoParcial() == null) {
            throw new IllegalArgumentException("El evento de cancelación debe indicar si requiere cargo parcial.");
        }
        UUID viajeId = parsearViajeId(evento.viajeId());
        if (evento.requiereCargoParcial()) {
            calculoTarifa.registrarCargoParcial(viajeId);
        }
    }

    private static void validarMedidas(Double distanciaKm, Double duracionMinutos) {
        if (distanciaKm == null || !Double.isFinite(distanciaKm) || distanciaKm < 0
                || duracionMinutos == null || !Double.isFinite(duracionMinutos) || duracionMinutos < 0) {
            throw new IllegalArgumentException("La distancia y la duración deben ser valores finitos no negativos.");
        }
    }

    private static UUID parsearViajeId(String viajeId) {
        try {
            return UUID.fromString(viajeId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El evento debe incluir un viajeId UUID válido.", e);
        }
    }
}
