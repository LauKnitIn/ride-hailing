package com.ride_hailing.tarifas.application;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.infraestructure.salida.persistence.TarifasCalculadas;

@Service
public class CalculoTarifaApplicationService implements CalculoTarifaUseCase {

    static final Monto TARIFA_MINIMA = new Monto(5000);
    static final double PORCENTAJE_CARGO_PARCIAL = 0.5;

    private final TarifasCalculadas tarifasCalculadas;
    private final CalculadoraTarifaService calculadora;

    public CalculoTarifaApplicationService(
            TarifasCalculadas tarifasCalculadas,
            CalculadoraTarifaService calculadora) {
        this.tarifasCalculadas = tarifasCalculadas;
        this.calculadora = calculadora;
    }

    @Override
    public Monto calcularTarifa(UUID viajeId) {
        DatosViajeTarifa viaje = tarifasCalculadas.buscarDatosViaje(viajeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No hay datos de finalización para el viaje " + viajeId));
        Monto monto = calcular(viaje);
        tarifasCalculadas.registrar(viajeId, monto);
        return monto;
    }

    @Override
    public void registrarViajeFinalizado(UUID viajeId, DatosViajeTarifa datosViaje) {
        tarifasCalculadas.registrarDatosViaje(viajeId, datosViaje);
        tarifasCalculadas.registrar(viajeId, calcular(datosViaje));
    }

    @Override
    public Monto calcularCargoParcial() {
        return calculadora.calcularCargoParcial(TARIFA_MINIMA, PORCENTAJE_CARGO_PARCIAL);
    }

    @Override
    public void registrarCargoParcial(UUID viajeId) {
        tarifasCalculadas.registrarCargoParcial(viajeId, calcularCargoParcial());
    }

    private Monto calcular(DatosViajeTarifa viaje) {
        return calculadora.calcularMonto(
                viaje.distanciaKm(),
                viaje.tiempoMinutos(),
                TARIFA_MINIMA,
                1500,
                300);
    }
}
