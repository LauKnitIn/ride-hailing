package com.ride_hailing.Conductores.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ride_hailing.Conductores.application.RegistrarConductorUseCase;
import com.ride_hailing.Conductores.domain.factory.ConductorFactory;
import com.ride_hailing.Conductores.domain.model.Conductor;
import com.ride_hailing.Conductores.infrastructure.web.dto.RegistrarConductorRequest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConductorController.class)
public class ConductorControllerTest {
   @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegistrarConductorUseCase registrarConductorUseCase;

    @Test
    @DisplayName("POST /api/v1/conductores - Debe registrar un conductor y retornar HTTP 201 Created")
    void registrarConductorEndpointExitoso() throws Exception {
        RegistrarConductorRequest request = new RegistrarConductorRequest(
            "CC",
            "1018400123",
            "Laura Barreto",
            LocalDate.of(1998, 5, 20)
        );

        Conductor conductorCreado = ConductorFactory.crearConductor(
            request.tipoDocumento(),
            request.numeroDocumento(),
            request.nombreCompleto(),
            request.fechaNacimiento()
        );

        when(registrarConductorUseCase.ejecutar(any(RegistrarConductorRequest.class)))
                .thenReturn(conductorCreado);

        // Act & Assert
        mockMvc.perform(post("/api/v1/conductores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idConductor").value(conductorCreado.getIdConductor().value()))
                .andExpect(jsonPath("$.numeroDocumento").value("1018400123"))
                .andExpect(jsonPath("$.nombreCompleto").value("Laura Barreto"));
    }

    @Test
    @DisplayName("POST /api/v1/conductores - Debe retornar HTTP 400 Bad Request cuando el payload es inválido")
    void registrarConductorEndpointPayloadInvalido() throws Exception {
        // Request con campos vacíos/nulos para disparar las anotaciones de validación
        RegistrarConductorRequest requestInvalido = new RegistrarConductorRequest(
            "",
            "",
            "",
            null
        );

        mockMvc.perform(post("/api/v1/conductores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}
