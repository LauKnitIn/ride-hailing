package com.ride_hailing.tarifas.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ride_hailing.tarifas.domain.CalculadoraTarifaService;
import com.ride_hailing.tarifas.domain.Monto;

@Service
public class CalculoTarifaApplicationService
        implements CalculoTarifaUseCase {

    private final RepositorioViajes repositorioViajes;
    private final CalculadoraTarifaService calculadora;


    public CalculoTarifaApplicationService(
            RepositorioViajes repositorioViajes,
            CalculadoraTarifaService calculadora) {
        this.repositorioViajes = repositorioViajes;
        this.calculadora = calculadora;
    }
    
    @Override
    public Monto calcularTarifa(UUID viajeId) {

        DatosViajeTarifa viaje =
                repositorioViajes.buscarPorId(viajeId);

        return calculadora.calcularMonto(
                viaje.distanciaKm(),
                viaje.tiempoMinutos(),
                new Monto(5000),
                1500,
                300
        );
    }
}
