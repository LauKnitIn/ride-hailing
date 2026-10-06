package com.ride_hailing.viajes;

import org.junit.jupiter.api.Test;

import com.ride_hailing.viajes.dominio.Ubicacion;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionTest {

    @Test
    void aceptaCoordenadasValidas() {
        Ubicacion ubicacion = new Ubicacion(5.5353, -73.3678); // Tunja
        assertEquals(5.5353, ubicacion.latitud());
    }

    @Test
    void rechazaLatitudFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> new Ubicacion(91, 0));
    }

    @Test
    void rechazaLongitudFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> new Ubicacion(0, -181));
    }

    @Test
    void laDistanciaEntreLaMismaUbicacionEsCero() {
        Ubicacion ubicacion = new Ubicacion(5.5353, -73.3678);
        assertEquals(0.0, ubicacion.distanciaHaciaKm(ubicacion), 0.0001);
    }

    @Test
    void calculaUnaDistanciaRazonableEntreDosCiudades() {
        Ubicacion tunja = new Ubicacion(5.5353, -73.3678);
        Ubicacion bogota = new Ubicacion(4.7110, -74.0721);

        double distanciaKm = tunja.distanciaHaciaKm(bogota);

        assertTrue(distanciaKm > 100 && distanciaKm < 140,
            "Se esperaba una distancia entre 100 y 140 km, fue " + distanciaKm);
    }
}
