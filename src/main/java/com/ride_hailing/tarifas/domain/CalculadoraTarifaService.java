package com.ride_hailing.tarifas.domain;

public class CalculadoraTarifaService {

    public Monto calcularTarifa(double distanciaKm, double tiempoMinutos, Monto tarifaMinima, double tarifaPorKm, double tarifaPorMinuto) {
        double costoDistancia = distanciaKm * tarifaPorKm;
        double costoTiempo = tiempoMinutos * tarifaPorMinuto;
        if (costoDistancia + costoTiempo < tarifaMinima.getValue()) {
            return tarifaMinima;
        }else{
            return new Monto(costoDistancia + costoTiempo);
        }
    }

}
