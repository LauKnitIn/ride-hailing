package com.ride_hailing.pasajeros;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegistroPasajeroServiceTest {

    private final RegistroPasajeroService service = new RegistroPasajeroService();
    private final PasajeroFactory factory = new PasajeroFactory(service);

    @Test
    void permiteRegistrarUnCorreoNuevo() {
        assertDoesNotThrow(() ->
            service.validarCorreoDisponible(new Correo("nuevo@correo.com"), List.of()));
    }

    @Test
    void rechazaUnCorreoYaRegistrado() {
        Pasajero existente = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());

        assertThrows(CorreoYaRegistradoException.class, () ->
            service.validarCorreoDisponible(new Correo("ana@correo.com"), List.of(existente)));
    }

    @Test
    void permiteCambiarDeCorreoAUnoQueNadieMasTiene() {
        Pasajero pasajero = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());

        service.validarCambioDeCorreo(pasajero, new Correo("ana.nueva@correo.com"), List.of(pasajero));

        assertEquals(new Correo("ana.nueva@correo.com"), pasajero.getCorreo());
    }

    @Test
    void noPermiteCambiarseAUnCorreoDeOtroPasajero() {
        Pasajero pasajero1 = factory.registrar("Ana Pérez", "ana@correo.com", "3001234567", List.of());
        Pasajero pasajero2 = factory.registrar("Luis Gómez", "luis@correo.com", "3009876543", List.of(pasajero1));

        assertThrows(CorreoYaRegistradoException.class, () ->
            service.validarCambioDeCorreo(pasajero2, new Correo("ana@correo.com"), List.of(pasajero1, pasajero2)));
    }
}
