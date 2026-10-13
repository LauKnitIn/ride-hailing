package com.ride_hailing.tarifas.infraestructure.salida.persistencia;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ride_hailing.tarifas.application.RepositorioTarifas;
import com.ride_hailing.tarifas.domain.Monto;
import com.ride_hailing.tarifas.domain.TarifaCalculada;

@Repository
public class TarifaPersistenceAdapter implements RepositorioTarifas {

    private final TarifaJpaRepository repository;

    public TarifaPersistenceAdapter(TarifaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardar(UUID viajeId, Monto monto) {
        repository.save(new TarifaCalculada(viajeId, monto));
    }
}
