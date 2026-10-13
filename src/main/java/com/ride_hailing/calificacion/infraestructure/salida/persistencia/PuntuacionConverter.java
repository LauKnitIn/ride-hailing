package com.ride_hailing.calificacion.infraestructure.salida.persistencia;

import com.ride_hailing.calificacion.domain.Puntuacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PuntuacionConverter implements AttributeConverter<Puntuacion, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Puntuacion puntuacion) {
        return puntuacion == null ? null : puntuacion.valor();
    }

    @Override
    public Puntuacion convertToEntityAttribute(Integer valor) {
        return valor == null ? null : new Puntuacion(valor);
    }
}
