package com.ride_hailing.calificacion;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.ride_hailing.calificacion.application.ServicioViajesNoDisponibleException;

@SpringBootTest(properties = "eureka.client.enabled=false")
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
    void consultaLasCalificacionesDeUnConductor() throws Exception {
        UUID conductorId = UUID.randomUUID();
        UUID viajeUno = UUID.randomUUID();
        UUID viajeDos = UUID.randomUUID();
        UUID pasajeroUno = UUID.randomUUID();
        UUID pasajeroDos = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeUno))
                .willReturn(new DatosViajeCalificacion(pasajeroUno, conductorId, true));
        given(repositorioViajes.buscarPorId(viajeDos))
                .willReturn(new DatosViajeCalificacion(pasajeroDos, conductorId, true));

        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeUno, pasajeroUno, 5)))
                .andExpect(status().isOk());
        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeDos, pasajeroDos, 4)))
                .andExpect(status().isOk());

        mvc.perform(get("/api/calificaciones/conductor/{conductorId}", conductorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].puntuacion").value(containsInAnyOrder(4, 5)))
                .andExpect(jsonPath("$[*].pasajeroId").doesNotExist());
    }

    @Test
    void devuelveListaVaciaSiElConductorAunNoTieneCalificaciones() throws Exception {
        mvc.perform(get("/api/calificaciones/conductor/{conductorId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
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
    void respondeServicioNoDisponibleCuandoViajesNoResponde() throws Exception {
        UUID viajeId = UUID.randomUUID();
        given(repositorioViajes.buscarPorId(viajeId))
                .willThrow(new ServicioViajesNoDisponibleException("Viajes no disponible", new RuntimeException()));

        mvc.perform(post("/api/calificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request(viajeId, UUID.randomUUID(), 5)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Viajes no disponible"));
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
