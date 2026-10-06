package com.ride_hailing.calificacion.infraestructure.salida;
import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.RepositorioViajes;

import org.springframework.stereotype.Component;

@Component("calificacionViajeAdapter")
public class ViajeAdapter implements RepositorioViajes {

    @Override
    public DatosViajeCalificacion buscarPorId(Long viajeId) {
        //Conectar con viajes posteriormente
        return new DatosViajeCalificacion(100L,200L);
    }

}
