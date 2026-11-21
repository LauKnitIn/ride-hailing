package com.ride_hailing.calificacion;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ride_hailing.calificacion.application.DatosViajeCalificacion;
import com.ride_hailing.calificacion.application.RepositorioViajes;

@SpringBootTest
@AutoConfigureMockMvc
class CalificacionesServiceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private RepositorioViajes repositorioViajes;

    @Test
    void calificaViajeFinalizadoYRechazaUnaSegundaCalificacion() throws Exception {
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        UUID conductorId = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeId))
                .willReturn(new DatosViajeCalificacion(pasajeroId, conductorId, true));

        String body = """
                {"viajeId":"%s","pasajeroId":"%s","puntuacion":5,"comentario":"Excelente"}
                """.formatted(viajeId, pasajeroId);

        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viajeId").value(viajeId.toString()))
                .andExpect(jsonPath("$.pasajeroId").value(pasajeroId.toString()))
                .andExpect(jsonPath("$.conductorId").value(conductorId.toString()))
                .andExpect(jsonPath("$.puntuacion").value(5));

        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void impideCalificarUnViajeNoFinalizado() throws Exception {
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeId))
                .willReturn(new DatosViajeCalificacion(pasajeroId, UUID.randomUUID(), false));

        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeId, pasajeroId, 5)))
                .andExpect(status().isConflict());
    }

    @Test
    void impideCalificarEnNombreDeOtroPasajero() throws Exception {
        UUID viajeId = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeId))
                .willReturn(new DatosViajeCalificacion(UUID.randomUUID(), UUID.randomUUID(), true));

        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeId, UUID.randomUUID(), 5)))
                .andExpect(status().isForbidden());
    }

    @Test
    void rechazaUnaPuntuacionFueraDelRango() throws Exception {
        UUID viajeId = UUID.randomUUID();
        UUID pasajeroId = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeId))
                .willReturn(new DatosViajeCalificacion(pasajeroId, UUID.randomUUID(), true));

        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeId, pasajeroId, 9)))
                .andExpect(status().isBadRequest());
    }

    private String request(UUID viajeId, UUID pasajeroId, int puntuacion) {
        return """
                {"viajeId":"%s","pasajeroId":"%s","puntuacion":%d,"comentario":"Prueba"}
                """.formatted(viajeId, pasajeroId, puntuacion);
    }
}
