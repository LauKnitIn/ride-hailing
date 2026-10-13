package com.ride_hailing.tarifas.infraestructure.salida.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ride_hailing.tarifas.domain.Monto;

/** Registro persistente de las tarifas y cargos parciales ya calculados por viaje. */
@Component
public class TarifasCalculadas {

    private final TarifaViajeJpaRepository jpaRepository;

    TarifasCalculadas(TarifaViajeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public void registrar(UUID viajeId, Monto monto) {
        TarifaViajeJpaEntity entidad = cargarOCrear(viajeId);
        entidad.tarifa = monto.valor();
        jpaRepository.save(entidad);
    }

    public Optional<Monto> buscar(UUID viajeId) {
        return jpaRepository.findById(viajeId).map(e -> e.tarifa).map(Monto::new);
    }

    public void registrarCargoParcial(UUID viajeId, Monto monto) {
        TarifaViajeJpaEntity entidad = cargarOCrear(viajeId);
        entidad.cargoParcial = monto.valor();
        jpaRepository.save(entidad);
    }

    public Optional<Monto> buscarCargoParcial(UUID viajeId) {
        return jpaRepository.findById(viajeId).map(e -> e.cargoParcial).map(Monto::new);
    }

    private TarifaViajeJpaEntity cargarOCrear(UUID viajeId) {
        return jpaRepository.findById(viajeId).orElseGet(() -> new TarifaViajeJpaEntity(viajeId));
    }
}
