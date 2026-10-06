package com.ride_hailing.emparejamientoOfertas.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ride_hailing.emparejamientoOfertas.application.AceptarOfertaService;
import com.ride_hailing.emparejamientoOfertas.application.ProcesarSolicitudEmparejamientoService;
import com.ride_hailing.emparejamientoOfertas.application.RechazarOfertaService;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.AceptarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.ProcesarSolicitudEmparejamientoUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.entrada.RechazarOfertaUseCase;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.application.puerto.salida.OfertaRepositoryPort;
import com.ride_hailing.emparejamientoOfertas.domain.factory.OfertaFactory;
import com.ride_hailing.emparejamientoOfertas.domain.service.SeleccionConductorDomainService;

@Configuration
public class EmparejamientoOfertasConfig {
    @Bean
    public SeleccionConductorDomainService seleccionConductorDomainService() {
        return new SeleccionConductorDomainService();
    }

    @Bean
    public OfertaFactory ofertaFactory() {
        return new OfertaFactory();
    }

    @Bean
    public ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase(
            ConductoresPort conductoresPort,
            OfertaRepositoryPort ofertaRepositoryPort,
            SeleccionConductorDomainService seleccionDomainService,
            OfertaFactory ofertaFactory) {
        return new ProcesarSolicitudEmparejamientoService(
                conductoresPort,
                ofertaRepositoryPort,
                seleccionDomainService,
                ofertaFactory
        );
    }

    @Bean
    public AceptarOfertaUseCase aceptarOfertaUseCase(
            OfertaRepositoryPort ofertaRepositoryPort,
            AsignacionViajePort asignacionViajePort) {
        return new AceptarOfertaService(ofertaRepositoryPort, asignacionViajePort);
    }

    @Bean
    public RechazarOfertaUseCase rechazarOfertaUseCase(
            OfertaRepositoryPort ofertaRepositoryPort,
            ProcesarSolicitudEmparejamientoUseCase procesarSolicitudEmparejamientoUseCase) {
        return new RechazarOfertaService(ofertaRepositoryPort, procesarSolicitudEmparejamientoUseCase);
    }
}
