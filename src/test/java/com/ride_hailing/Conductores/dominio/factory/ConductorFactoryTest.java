package com.ride_hailing.Conductores.dominio.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.ride_hailing.Conductores.domain.factory.ConductorFactory;
import com.ride_hailing.Conductores.domain.model.Conductor;

public class ConductorFactoryTest {
    @Test
    @DisplayName("Debe crear un Conductor válido con DriverId asignado automáticamente")
    void crearConductorExitoso() {
        String tipoDoc = "CEDULA_CIUDADANIA";
        String numDoc = "1018400123";
        String nombre = "Laura Barreto";
        LocalDate fechaNacimiento = LocalDate.of(1998, 5, 20);

        Conductor conductor = ConductorFactory.crearConductor(tipoDoc, numDoc, nombre, fechaNacimiento);

        assertNotNull(conductor);
        assertNotNull(conductor.getIdConductor());
        assertNotNull(conductor.getIdConductor().value());
        assertEquals(numDoc, conductor.getDocumentoIdentidad().getNumeroDocumento());
        assertEquals(nombre, conductor.getNombreCompleto());
    }

    @Test
    @DisplayName("Debe lanzar excepción al pasar un tipo de documento inválido")
    void crearConductorTipoDocumentoInvalido() {
        // Arrange
        String tipoDocInvalido = "INVALIDO";
        String numDoc = "1018400123";
        String nombre = "Laura Barreto";
        LocalDate fechaNacimiento = LocalDate.of(1998, 5, 20);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            ConductorFactory.crearConductor(tipoDocInvalido, numDoc, nombre, fechaNacimiento)
        );
    }
}
