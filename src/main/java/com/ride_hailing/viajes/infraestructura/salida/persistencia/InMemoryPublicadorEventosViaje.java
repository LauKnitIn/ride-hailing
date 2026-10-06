package com.ride_hailing.viajes.infraestructura.salida.persistencia;

import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.PublicadorEventosViaje;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InMemoryPublicadorEventosViaje implements PublicadorEventosViaje {

    private final List<Object> eventosPublicados = new CopyOnWriteArrayList<>();

    @Override
    public void publicar(Object evento) {
        eventosPublicados.add(evento);
    }

    public List<Object> eventosPublicados() {
        return Collections.unmodifiableList(eventosPublicados);
    }
}
