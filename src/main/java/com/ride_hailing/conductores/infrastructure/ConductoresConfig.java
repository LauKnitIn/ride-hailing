package com.ride_hailing.conductores.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ride_hailing.conductores.application.ConductorFactory;
import com.ride_hailing.conductores.application.ConductorService;
import com.ride_hailing.conductores.application.puerto.entrada.ConductorUseCase;
import com.ride_hailing.conductores.application.puerto.salida.ConductorRepositoryPort;
import com.ride_hailing.conductores.application.puerto.salida.ViajeActivoPort;
import com.ride_hailing.conductores.domain.service.ConductorDomainService;

@Configuration
public class ConductoresConfig {

    @Bean
    ConductorDomainService conductorDomainService() {
        return new ConductorDomainService();
    }

    @Bean
    ConductorFactory conductorFactory(ConductorRepositoryPort conductorRepository) {
        return new ConductorFactory(conductorRepository);
    }

    @Bean
    ConductorUseCase conductorUseCase(ConductorRepositoryPort conductorRepository,
                                      ViajeActivoPort viajeActivoPort,
                                      ConductorFactory conductorFactory,
                                      ConductorDomainService conductorDomainService) {
        return new ConductorService(conductorRepository, viajeActivoPort, conductorFactory, conductorDomainService);
    }
}

