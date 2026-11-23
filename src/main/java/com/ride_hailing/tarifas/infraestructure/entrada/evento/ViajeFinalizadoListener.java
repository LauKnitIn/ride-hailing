package com.ride_hailing.tarifas.infraestructure.entrada.evento;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ride_hailing.tarifas.application.CalculoTarifaUseCase;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.infraestructure.salida.persistence.TarifasCalculadas;
import com.ride_hailing.viajes.aplicacion.ViajeFinalizadoEvent;

/** Al finalizar un viaje calcula y registra su tarifa automaticamente (HU-07). */
@Component
public class ViajeFinalizadoListener {

    private final CalculoTarifaUseCase calculoTarifa;
    private final TarifasCalculadas tarifasCalculadas;

    public ViajeFinalizadoListener(CalculoTarifaUseCase calculoTarifa, TarifasCalculadas tarifasCalculadas) {
        this.calculoTarifa = calculoTarifa;
        this.tarifasCalculadas = tarifasCalculadas;
    }

    @EventListener
    public void alFinalizarViaje(ViajeFinalizadoEvent evento) {
        Monto monto = calculoTarifa.calcularTarifa(evento.viajeId());
        tarifasCalculadas.registrar(evento.viajeId(), monto);
    }
}
