package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ride_hailing.pasajeros.aplicacion.PasajeroFactory;
import com.ride_hailing.pasajeros.aplicacion.PasajeroRepository;
import com.ride_hailing.pasajeros.aplicacion.PasajeroService;
import com.ride_hailing.pasajeros.aplicacion.RegistroPasajeroService;
import com.ride_hailing.pasajeros.dominio.CorreoYaRegistradoException;
import com.ride_hailing.pasajeros.dominio.Pasajero;
import com.ride_hailing.pasajeros.dominio.PasajeroNoEncontradoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasajeroServiceTest {

    private PasajeroRepository pasajeroRepository;
    private PasajeroService pasajeroService;

    @BeforeEach
    void setUp() {
        pasajeroRepository = mock(PasajeroRepository.class);
        pasajeroService = new PasajeroService(
            pasajeroRepository, new PasajeroFactory(new RegistroPasajeroService()), new RegistroPasajeroService());
    }

    @Test
    void registrarConsultaLosExistentesYGuarda() {
        when(pasajeroRepository.listarTodos()).thenReturn(List.of());

        Pasajero pasajero = pasajeroService.registrar("Ana Pérez", "ana@correo.com", "3001234567");

        verify(pasajeroRepository).listarTodos();
        verify(pasajeroRepository).guardar(pasajero);
    }

    @Test
    void rechazaRegistrarConCorreoYaExistenteSegunElRepositorio() {
        Pasajero existente = new PasajeroFactory(new RegistroPasajeroService())
            .registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());
        when(pasajeroRepository.listarTodos()).thenReturn(List.of(existente));

        assertThrows(CorreoYaRegistradoException.class,
            () -> pasajeroService.registrar("Otra Ana", "ana@correo.com", "3009876543"));
    }

    @Test
    void consultarPasajeroInexistenteLanzaExcepcion() {
        UUID id = UUID.randomUUID();
        when(pasajeroRepository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(PasajeroNoEncontradoException.class, () -> pasajeroService.consultar(id));
    }

}
