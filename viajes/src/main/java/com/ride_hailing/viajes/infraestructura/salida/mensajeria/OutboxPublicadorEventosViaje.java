package com.ride_hailing.viajes.infraestructura.salida.mensajeria;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.PublicadorEventosViaje;
import com.ride_hailing.viajes.aplicacion.SolicitudRechazadaEvent;
import com.ride_hailing.viajes.aplicacion.ViajeAsignadoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;
import com.ride_hailing.viajes.aplicacion.ViajeSolicitadoEvent;

import tools.jackson.databind.ObjectMapper;

@Component
public class OutboxPublicadorEventosViaje implements PublicadorEventosViaje {

    private final OutboxEventRepository repositorio;
    private final ObjectMapper objectMapper;

    public OutboxPublicadorEventosViaje(OutboxEventRepository repositorio, ObjectMapper objectMapper) {
        this.repositorio = repositorio;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(Object evento) {
        switch (evento) {
            case ViajeSolicitadoEvent e -> guardar("viajes.solicitados", e.viajeId(), Map.of(
                    "viajeId", e.viajeId().toString(),
                    "pasajeroId", e.pasajeroId().toString(),
                    "latitudOrigen", e.latitudOrigen(),
                    "longitudOrigen", e.longitudOrigen(),
                    "momento", e.momento().toString()));
            case ViajeAsignadoEvent e -> guardar("viajes.asignados", e.viajeId(), Map.of(
                    "viajeId", e.viajeId().toString(),
                    "conductorId", e.conductorId().toString(),
                    "momento", e.momento().toString()));
            case ViajeFinalizadoEvent e -> guardar("viajes.finalizados", e.viajeId(), Map.of(
                    "viajeId", e.viajeId().toString(),
                    "conductorId", e.conductorId().toString(),
                    "distanciaKm", e.distanciaKm(),
                    "duracionMinutos", e.duracion().toMillis() / 60000.0,
                    "momento", e.momento().toString()));
            case ViajeCanceladoEvent e -> {
                // Map.of no admite null, y conductorId es null si se canceló antes de asignar
                Map<String, Object> contenido = new LinkedHashMap<>();
                contenido.put("viajeId", e.viajeId().toString());
                contenido.put("conductorId", e.conductorId() == null ? null : e.conductorId().toString());
                contenido.put("requiereCargoParcial", e.requiereCargoParcial());
                contenido.put("momento", e.momento().toString());
                guardar("viajes.cancelados", e.viajeId(), contenido);
            }
            case SolicitudRechazadaEvent e -> guardar("viajes.rechazados", e.viajeId(), Map.of(
                    "viajeId", e.viajeId().toString(),
                    "motivo", e.motivo(),
                    "momento", e.momento().toString()));
            default -> throw new IllegalArgumentException(
                    "Evento de viaje sin tópico asignado: " + evento.getClass().getSimpleName());
        }
    }

    private void guardar(String topico, UUID viajeId, Map<String, Object> contenido) {
        repositorio.save(new OutboxEvent(topico, viajeId.toString(), objectMapper.writeValueAsString(contenido)));
    }
}