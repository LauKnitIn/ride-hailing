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

    /** Cargo parcial por cancelar tras la asignacion: una fraccion (0 a 1) de la tarifa minima. */
    public Monto calcularCargoParcial(Monto montoMinimo, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 1) {
            throw new IllegalArgumentException("El porcentaje del cargo parcial debe estar entre 0 y 1.");
        }
        return new Monto(montoMinimo.getValue() * porcentaje);
    }

}
