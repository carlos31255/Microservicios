package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.service.CiudadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CiudadController.class)
public class CiudadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CiudadService ciudadService;

    @Test
    void obtenerTodasLasCiudades_returnsOk() throws Exception {
        CiudadDTO c = new CiudadDTO(1L, "Santiago", 1L);
        when(ciudadService.obtenerTodasLasCiudades()).thenReturn(List.of(c));

        mockMvc.perform(get("/geografia/ciudades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Santiago"));
    }

    @Test
    void obtenerCiudadPorId_returnsOk() throws Exception {
        CiudadDTO c = new CiudadDTO(2L, "Valparaíso", 2L);
        when(ciudadService.obtenerCiudadPorId(anyLong())).thenReturn(c);

        mockMvc.perform(get("/geografia/ciudades/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Valparaíso"));
    }

    @Test
    void obtenerCiudadesPorRegion_returnsOk() throws Exception {
        CiudadDTO c = new CiudadDTO(3L, "Concepción", 3L);
        when(ciudadService.obtenerCiudadesPorRegion(3L)).thenReturn(List.of(c));

        mockMvc.perform(get("/geografia/ciudades/region/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].regionId").value(3));
    }
}
