package com.ride_hailing.emparejamientoOfertas.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ride_hailing.emparejamientoOfertas.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.aplicacion.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.aplicacion.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.dominio.EmparejamientoDomainService;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@Configuration
public class EmparejamientoConfig {
    @Bean
    public EmparejamientoDomainService emparejamientoDomainService() {
        return new EmparejamientoDomainService();
    }

    @Bean
    public ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase(
            EmparejamientoDomainService domainService,
            OfertaRepository ofertaRepository,
            ConductoresPort conductoresPort,
            AsignacionViajePort asignacionViajePort,
            @Value("${emparejamiento.radio-busqueda-km:5.0}") double radioKm) {
        return new ProcesarSolicitudEmparejamientoUseCase(
                domainService, ofertaRepository, conductoresPort, asignacionViajePort, radioKm);
    }

    @Bean
    public AceptarOfertaUseCase aceptarOfertaUseCase(
            OfertaRepository ofertaRepository,
            AsignacionViajePort asignacionViajePort) {
        return new AceptarOfertaUseCase(ofertaRepository, asignacionViajePort);
    }

    @Bean
    public RechazarOfertaUseCase rechazarOfertaUseCase(
            OfertaRepository ofertaRepository,
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudUseCase) {
        return new RechazarOfertaUseCase(ofertaRepository, procesarSolicitudUseCase);
    }
}
