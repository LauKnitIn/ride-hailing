package com.ride_hailing.tarifas.infraestructure.entrada.evento;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
class TarifasServiceClientConfiguration {

    @Bean
    RestClient.Builder tarifasServiceRestClientBuilder() {
        return RestClient.builder();
    }
}
