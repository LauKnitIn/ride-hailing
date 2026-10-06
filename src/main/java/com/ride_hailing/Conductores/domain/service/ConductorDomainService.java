package com.ride_hailing.Conductores.domain;

import com.ride_hailing.Conductores.domain.Event.DisponibilidadDesactivadaEvent;
import com.ride_hailing.Conductores.domain.Event.DisponibilidadRegistradaEvent;
import com.ride_hailing.Conductores.domain.Event.UbicacionConductorActualizadaEvent;
import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.domain.model.UbicacionGeografica;

public class ConductorDomainService {
    private final ViajeActivoCheckerRepository viajeActivoChecker;

    public ConductorDomainService(ViajeActivoCheckerRepository viajeActivoChecker) {
        this.viajeActivoChecker = viajeActivoChecker;
    }

    public DisponibilidadRegistradaEvent registrarDisponibilidad(Conductor conductor) {
        if (viajeActivoChecker.tieneViajeEnCurso(conductor.getIdConductor())) {
            throw new IllegalStateException("El conductor no puede registrar disponibilidad mientras tiene un viaje en curso.");
        }

        conductor.registrarDisponibilidad();

        return new DisponibilidadRegistradaEvent(conductor.getIdConductor(), conductor.getUbicacionActual());
    }

    public DisponibilidadDesactivadaEvent desactivarDisponibilidad(Conductor conductor) {
        if (viajeActivoChecker.tieneViajeEnCurso(conductor.getIdConductor())) {
            throw new IllegalStateException("No se puede desactivar la disponibilidad durante un viaje activo.");
        }

        conductor.desactivarDisponibilidad();

        return new DisponibilidadDesactivadaEvent(conductor.getIdConductor());
    }


    public UbicacionConductorActualizadaEvent actualizarUbicacion(Conductor conductor, UbicacionGeografica nuevaUbicacion) {
        conductor.actualizarUbicacion(nuevaUbicacion);

        return new UbicacionConductorActualizadaEvent(conductor.getIdConductor(), nuevaUbicacion);
    }
}
