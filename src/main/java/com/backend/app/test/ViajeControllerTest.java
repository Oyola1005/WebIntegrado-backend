package com.backend.app.test;

import com.backend.app.model.Viaje;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ViajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Listar viajes iniciales devuelve un array con 2 elementos")
    void debeListarViajesIniciales() throws Exception {
        mockMvc.perform(get("/api/viajes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Crear viaje devuelve el viaje creado con id generado")
    void debeCrearNuevoViaje() throws Exception {
        Viaje nuevo = new Viaje(
                null,
                "Lima",
                "Chimbote",
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(8),
                90.0,
                35,
                "PROGRAMADO"
        );

        String body = objectMapper.writeValueAsString(nuevo);

        mockMvc.perform(
                        post("/api/viajes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.origen").value("Lima"))
                .andExpect(jsonPath("$.destino").value("Chimbote"));
    }
}