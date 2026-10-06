package com.ride_hailing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ride_hailing.emparejamientoOfertas.AsignacionViajePort;
import com.ride_hailing.emparejamientoOfertas.ConductoresPort;
import com.ride_hailing.emparejamientoOfertas.dominio.OfertaRepository;

@SpringBootTest
class MatchingRidesApplicationTests {

	@MockitoBean
    private ConductoresPort conductoresPort;

    @MockitoBean
    private AsignacionViajePort asignacionViajePort;

    @MockitoBean
    private OfertaRepository ofertaRepository;

    @Test
    void contextLoads() {
    }

}
