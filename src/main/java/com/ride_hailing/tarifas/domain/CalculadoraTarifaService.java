package com.ride_hailing.tarifas.domain;

import org.springframework.stereotype.Service;

@Service
public class CalculadoraTarifaService {

    public Monto calcularMonto(double distanciaKm, double tiempoMinutos, Monto montoMinimo, double tarifaPorKm, double tarifaPorMinuto) {
        double costoDistancia = distanciaKm * tarifaPorKm;
        double costoTiempo = tiempoMinutos * tarifaPorMinuto;
        if (costoDistancia + costoTiempo < montoMinimo.getValue()) {
            return montoMinimo;
        }else{
            return new Monto(costoDistancia + costoTiempo);
        }
    }

}
