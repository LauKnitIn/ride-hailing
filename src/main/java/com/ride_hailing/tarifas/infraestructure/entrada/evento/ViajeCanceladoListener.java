package com.ride_hailing.tarifas.infraestructure.entrada.evento;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.infraestructure.salida.persistence.TarifasCalculadas;
import com.ride_hailing.viajes.aplicacion.ViajeCanceladoEvent;

/** Si el viaje se cancela despues de la asignacion, registra el cargo parcial al pasajero (HU-10). */
@Component
public class ViajeCanceladoListener {

    private final CalculoTarifaUseCase calculoTarifa;
    private final TarifasCalculadas tarifasCalculadas;

    public ViajeCanceladoListener(CalculoTarifaUseCase calculoTarifa, TarifasCalculadas tarifasCalculadas) {
        this.calculoTarifa = calculoTarifa;
        this.tarifasCalculadas = tarifasCalculadas;
    }

    @EventListener
    public void alCancelarViaje(ViajeCanceladoEvent evento) {
        if (evento.requiereCargoParcial()) {
            tarifasCalculadas.registrarCargoParcial(evento.viajeId(), calculoTarifa.calcularCargoParcial());
        }
    }
}
