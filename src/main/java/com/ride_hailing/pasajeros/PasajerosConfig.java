package com.ride_hailing.pasajeros;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class PasajerosConfig {
    @Bean
    public PasajeroRepository pasajeroRepository() {
        return new InMemoryPasajeroRepository();
    }

    @Bean
    public RegistroPasajeroService registroPasajeroService() {
        return new RegistroPasajeroService();
    }

    @Bean
    public PasajeroFactory pasajeroFactory(RegistroPasajeroService registroPasajeroService) {
        return new PasajeroFactory(registroPasajeroService);
    }

    @Bean
    public PasajeroUseCase pasajeroUseCase(PasajeroRepository pasajeroRepository,
                                           PasajeroFactory pasajeroFactory,
                                           RegistroPasajeroService registroPasajeroService) {
        return new PasajeroService(pasajeroRepository, pasajeroFactory, registroPasajeroService);
    }
}
