package com.ride_hailing.emparejamientoOfertas.application;

import java.util.ArrayList;
import java.util.List;

import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;

public class AsignacionViajePortFake implements AsignacionViajePort {

    private final List<String> logAsignados;
    private final List<String> logRechazados;

    public AsignacionViajePortFake(List<String> logAsignados, List<String> logRechazados) {
        this.logAsignados = logAsignados;
        this.logRechazados = logRechazados;
    }

    @Override
    public void asignar(String viajeId, String conductorId) {
        logAsignados.add(viajeId + ":" + conductorId);
    }

    @Override
    public void rechazarPorFaltaDeConductores(String viajeId, String motivo) {
        logRechazados.add(viajeId + ":" + motivo);
    }

}
