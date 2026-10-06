package com.ride_hailing.tarifas.application;

import java.util.UUID;

import com.ride_hailing.tarifas.domain.Monto;

public interface CalculoTarifaUseCase {

    Monto calcularTarifa(UUID viajeId);
}