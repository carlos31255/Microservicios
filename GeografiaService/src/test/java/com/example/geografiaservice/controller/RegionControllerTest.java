package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.service.RegionService;
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

@WebMvcTest(controllers = RegionController.class)
public class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionService regionService;

    @Test
    void obtenerTodasLasRegiones_returnsOk() throws Exception {
        RegionDTO r = new RegionDTO(1L, "Región Metropolitana", "RM", 1);
        when(regionService.obtenerTodasLasRegiones()).thenReturn(List.of(r));

        mockMvc.perform(get("/geografia/regiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Región Metropolitana"));
    }

    @Test
    void obtenerRegionPorId_returnsOk() throws Exception {
        RegionDTO r = new RegionDTO(2L, "Valparaíso", "VS", 2);
        when(regionService.obtenerRegionPorId(anyLong())).thenReturn(r);

        mockMvc.perform(get("/geografia/regiones/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Valparaíso"));
    }

    @Test
    void obtenerRegionPorCodigo_returnsOk() throws Exception {
        RegionDTO r = new RegionDTO(3L, "Biobío", "BB", 3);
        when(regionService.obtenerRegionPorCodigo("BB")).thenReturn(r);

        mockMvc.perform(get("/geografia/regiones/codigo/BB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("BB"))
                .andExpect(jsonPath("$.nombre").value("Biobío"));
    }
}
