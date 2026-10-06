 package com.ride_hailing.viajes;

import org.springframework.context.annotation.Bean;

public class ViajesConfig {
    @Bean
    public ViajeRepository viajeRepository() {
        return new InMemoryViajeRepository();
    }

    @Bean
    public ViajeFactory viajeFactory() {
        return new ViajeFactory();
    }

    @Bean
    public TransicionEstadoViajeService transicionEstadoViajeService() {
        return new TransicionEstadoViajeService();
    }

    @Bean
    public ViajeUseCase viajeUseCase(ViajeRepository viajeRepository,
                                     ViajeFactory viajeFactory,
                                     TransicionEstadoViajeService transicionEstadoViajeService) {
        return new ViajeService(viajeRepository, viajeFactory, transicionEstadoViajeService);
    }
}
