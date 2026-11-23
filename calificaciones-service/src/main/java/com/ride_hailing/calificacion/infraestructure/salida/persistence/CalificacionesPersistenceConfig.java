package com.ride_hailing.calificacion.infraestructure.salida.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = CalificacionJpaRepository.class)
class CalificacionesPersistenceConfig {
}
