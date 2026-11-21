package com.ride_hailing.calificacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.ride_hailing.calificacion.domain.Puntuacion;

class PuntuacionTest {

    @Test
    void aceptaPuntuacionesEntreUnaYCinco() {
        assertThat(new Puntuacion(1).valor()).isEqualTo(1);
        assertThat(new Puntuacion(5).valor()).isEqualTo(5);
    }

    @Test
    void rechazaPuntuacionesFueraDelRango() {
        assertThatThrownBy(() -> new Puntuacion(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Puntuacion(6)).isInstanceOf(IllegalArgumentException.class);
    }
}
