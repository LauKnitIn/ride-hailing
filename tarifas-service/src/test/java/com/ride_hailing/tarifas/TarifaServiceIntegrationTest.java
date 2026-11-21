package com.ride_hailing.tarifas;

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
import org.springframework.test.web.servlet.MockMvc;

import com.ride_hailing.tarifas.infraestructure.entrada.mensajeria.ViajesKafkaConsumer;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:tarifasdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.kafka.listener.auto-startup=false",
        "eureka.client.enabled=false"
})
@AutoConfigureMockMvc
class TarifaServiceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ViajesKafkaConsumer viajesKafkaConsumer;

    @Test
    void finalizacionAlimentaLaTarifaQueConsultanLosClientes() throws Exception {
        UUID viajeId = UUID.randomUUID();

        mvc.perform(post("/internal/viajes/{id}/finalizado", viajeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distanciaKm\":10.0,\"tiempoMinutos\":20.0}"))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/tarifas/{id}", viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viajeId").value(viajeId.toString()))
                .andExpect(jsonPath("$.monto").value(21000.0));

        mvc.perform(post("/api/tarifas/{id}/calcular", viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(21000.0));
    }

    @Test
    void cancelacionConCargoParcialAlimentaElEndpointDeCargo() throws Exception {
        UUID viajeId = UUID.randomUUID();

        mvc.perform(post("/internal/viajes/{id}/cancelado", viajeId))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/tarifas/{id}/cargo-parcial", viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(2500.0));
    }

    @Test
    void calcularUnViajeSinDatosDeFinalizacionRespondeNoEncontrado() throws Exception {
        mvc.perform(post("/api/tarifas/{id}/calcular", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void reentregarUnEventoFinalizadoActualizaLaMismaTarifaDeFormaIdempotente() throws Exception {
        UUID viajeId = UUID.randomUUID();
        String evento = """
                {"viajeId":"%s","distanciaKm":10.0,"duracionMinutos":20.0}
                """.formatted(viajeId);

        viajesKafkaConsumer.alFinalizarViaje(evento);
        viajesKafkaConsumer.alFinalizarViaje(evento);

        mvc.perform(get("/api/tarifas/{id}", viajeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(21000.0));
    }
}
