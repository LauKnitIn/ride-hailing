package com.ride_hailing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

/** HU-01 a HU-04 de punta a punta: viajes, conductores y emparejamiento conectados. */
@SpringBootTest
@AutoConfigureMockMvc
class EmparejamientoFlujoCompletoTest {

    @Autowired
    private MockMvc mvc;

    private String registrarConductorDisponible(String documento, double lat, double lon) throws Exception {
        String body = """
            {"nombreCompleto":"Conductor %s","tipoDocumento":"CEDULA_CIUDADANIA",
             "numeroDocumento":"%s","fechaNacimiento":"1990-01-01"}""".formatted(documento, documento);
        String respuesta = mvc.perform(post("/api/conductores").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(respuesta, "$.id");

        mvc.perform(put("/api/conductores/" + id + "/disponibilidad").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitud\":%s,\"longitud\":%s}".formatted(lat, lon)))
                .andExpect(status().isOk());
        return id;
    }

    private String solicitarViaje(double lat, double lon) throws Exception {
        String body = """
            {"pasajeroId":"%s","latitudOrigen":%s,"longitudOrigen":%s,
             "latitudDestino":%s,"longitudDestino":%s}""".formatted(UUID.randomUUID(), lat, lon, lat + 0.01, lon + 0.01);
        String respuesta = mvc.perform(post("/api/viajes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return JsonPath.read(respuesta, "$.id");
    }

    @Test
    void ofertaAlConductorMasCercanoYAlAceptarElViajeQuedaAsignado() throws Exception {
        String cercano = registrarConductorDisponible("1001", 40.0010, 10.0010);
        registrarConductorDisponible("1002", 41.0000, 11.0000);

        String viajeId = solicitarViaje(40.0, 10.0);

        String ofertas = mvc.perform(get("/api/ofertas/viaje/" + viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].conductorId").value(cercano))
                .andExpect(jsonPath("$[0].estado").value("PROPUESTA"))
                .andReturn().getResponse().getContentAsString();
        String ofertaId = JsonPath.read(ofertas, "$[0].id");

        mvc.perform(post("/api/ofertas/" + ofertaId + "/aceptar")).andExpect(status().isOk());

        mvc.perform(get("/api/viajes/" + viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ASIGNADO"));

        // Al aceptar, el conductor deja de estar disponible (no recibe otra oferta).
        mvc.perform(get("/api/conductores/" + cercano))
                .andExpect(jsonPath("$.disponibilidad").value("EN_VIAJE"));

        // HU-05: el pasajero ve la ubicación del conductor y el tiempo estimado de llegada.
        mvc.perform(get("/api/viajes/" + viajeId + "/ubicacion-conductor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitud").value(40.0010))
                .andExpect(jsonPath("$.longitud").value(10.0010))
                .andExpect(jsonPath("$.etaMinutos").isNumber());

        // El conductor reporta una nueva ubicación y el seguimiento la refleja.
        mvc.perform(put("/api/conductores/" + cercano + "/ubicacion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitud\":40.0005,\"longitud\":10.0005}"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/viajes/" + viajeId + "/ubicacion-conductor"))
                .andExpect(jsonPath("$.latitud").value(40.0005));

        // HU-08: no se puede calificar un viaje que no ha finalizado.
        String viaje = mvc.perform(get("/api/viajes/" + viajeId)).andReturn().getResponse().getContentAsString();
        String pasajeroId = JsonPath.read(viaje, "$.pasajeroId");
        String calificacion = "{\"viajeId\":\"%s\",\"pasajeroId\":\"%s\",\"puntuacion\":5,\"comentario\":\"Excelente\"}"
                .formatted(viajeId, pasajeroId);
        String deOtroPasajero = calificacion.replace(pasajeroId, UUID.randomUUID().toString());
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(calificacion))
                .andExpect(status().isConflict());

        mvc.perform(post("/api/viajes/" + viajeId + "/iniciar")).andExpect(status().isNoContent());
        mvc.perform(post("/api/viajes/" + viajeId + "/finalizar")).andExpect(status().isNoContent());

        // Al finalizar, el conductor vuelve a estar disponible y ya no hay seguimiento.
        mvc.perform(get("/api/conductores/" + cercano))
                .andExpect(jsonPath("$.disponibilidad").value("DISPONIBLE"));
        mvc.perform(get("/api/viajes/" + viajeId + "/ubicacion-conductor")).andExpect(status().isConflict());

        // HU-07: la tarifa se calcula sola al finalizar (mínimo configurado: 5000).
        mvc.perform(get("/api/tarifas/" + viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(org.hamcrest.Matchers.greaterThanOrEqualTo(5000.0)));

        // HU-08: solo el pasajero del viaje puede calificar; luego, una vez y rechazada la segunda.
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(deOtroPasajero))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(calificacion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conductorId").value(cercano));
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content(calificacion))
                .andExpect(status().isConflict());
    }

    @Test
    void alRechazarSeReofreceAlSiguienteYSinMasConductoresElViajeSeCancela() throws Exception {
        String unico = registrarConductorDisponible("2001", -30.0010, -60.0010);
        // Los conductores de otros tests siguen disponibles, así que se rechazan en cadena hasta agotarlos.
        String viajeId = solicitarViaje(-30.0, -60.0);

        String actual = ofertaPendiente(viajeId);
        int guardas = 0;
        while (actual != null && guardas++ < 50) {
            var res = mvc.perform(post("/api/ofertas/" + actual + "/rechazar")).andReturn().getResponse();
            actual = res.getStatus() == 200 ? JsonPath.read(res.getContentAsString(), "$.id") : null;
        }

        mvc.perform(get("/api/viajes/" + viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"));
        mvc.perform(get("/api/conductores/" + unico)).andExpect(status().isOk());
    }

    @Test
    void elPasajeroRegistradoSePersisteYSePuedeConsultarYActualizar() throws Exception {
        String correo = "pasajero" + UUID.randomUUID() + "@test.com";
        String respuesta = mvc.perform(post("/api/pasajeros").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"correo\":\"%s\",\"telefono\":\"3001234567\"}".formatted(correo)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(respuesta, "$.id");

        mvc.perform(get("/api/pasajeros/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value(correo));

        mvc.perform(put("/api/pasajeros/" + id + "/perfil").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana Maria\",\"telefono\":\"3109876543\"}"))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/pasajeros/" + id))
                .andExpect(jsonPath("$.nombre").value("Ana Maria"));
    }

    @Test
    void cancelarTrasLaAsignacionGeneraCargoParcialYLiberaAlConductor() throws Exception {
        String conductor = registrarConductorDisponible("3001", 60.0010, -100.0010);
        String viajeId = solicitarViaje(60.0, -100.0);
        mvc.perform(post("/api/ofertas/" + ofertaPendiente(viajeId) + "/aceptar")).andExpect(status().isOk());

        mvc.perform(post("/api/viajes/" + viajeId + "/cancelar").contentType(MediaType.TEXT_PLAIN)
                        .content("El pasajero cambió de planes"))
                .andExpect(status().isNoContent());

        // 50% de la tarifa mínima (5000).
        mvc.perform(get("/api/tarifas/" + viajeId + "/cargo-parcial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(2500.0));
        mvc.perform(get("/api/conductores/" + conductor))
                .andExpect(jsonPath("$.disponibilidad").value("DISPONIBLE"));
    }

    @Test
    void cancelarAntesDeLaAsignacionNoGeneraCargo() throws Exception {
        registrarConductorDisponible("3002", 70.0010, -110.0010);
        String viajeId = solicitarViaje(70.0, -110.0); // queda con oferta PROPUESTA, sin aceptar

        mvc.perform(post("/api/viajes/" + viajeId + "/cancelar").contentType(MediaType.TEXT_PLAIN)
                        .content("Ya no lo necesito"))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/tarifas/" + viajeId + "/cargo-parcial")).andExpect(status().isNotFound());
    }

    private String ofertaPendiente(String viajeId) throws Exception {
        String ofertas = mvc.perform(get("/api/ofertas/viaje/" + viajeId)).andReturn().getResponse().getContentAsString();
        java.util.List<String> ids = JsonPath.read(ofertas, "$[?(@.estado=='PROPUESTA')].id");
        return ids.isEmpty() ? null : ids.get(0);
    }
}
