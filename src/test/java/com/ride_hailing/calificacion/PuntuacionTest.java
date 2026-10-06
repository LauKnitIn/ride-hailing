package com.ride_hailing.calificacion;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.ride_hailing.calificacion.domain.Puntuacion;

import static org.assertj.core.api.Assertions.*;

class PuntuacionTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void deberiaCrearPuntuacionValida(int valorValido) {
        Puntuacion puntuacion = new Puntuacion(valorValido);

        assertThat(puntuacion.valor()).isEqualTo(valorValido);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 6, 10})
    void deberiaLanzarExcepcionCuandoPuntuacionEsInvalida(int valorInvalido) {
        assertThatThrownBy(() -> new Puntuacion(valorInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La puntuación debe estar comprendida entre 1 y 5 estrellas.");
    }
}