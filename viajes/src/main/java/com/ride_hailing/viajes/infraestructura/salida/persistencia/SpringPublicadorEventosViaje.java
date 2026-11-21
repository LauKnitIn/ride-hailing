package com.ride_hailing.viajes.infraestructura.salida.persistencia;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.ride_hailing.viajes.aplicacion.PublicadorEventosViaje;

/** Publica los eventos de viaje al bus de eventos de Spring para que otros módulos reaccionen. */
@Component
@Primary
public class SpringPublicadorEventosViaje implements PublicadorEventosViaje {

    private final ApplicationEventPublisher publisher;

    public SpringPublicadorEventosViaje(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(Object evento) {
        publisher.publishEvent(evento);
    }
}
