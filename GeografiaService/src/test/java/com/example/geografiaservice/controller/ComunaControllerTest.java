package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.ComunaDTO;
import com.example.geografiaservice.service.ComunaService;
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

@WebMvcTest(controllers = ComunaController.class)
public class ComunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComunaService comunaService;

    @Test
    void obtenerTodasLasComunas_returnsOk() throws Exception {
        ComunaDTO c = new ComunaDTO(1L, "Providencia", 1L, 1L);
        when(comunaService.obtenerTodasLasComunas()).thenReturn(List.of(c));

        mockMvc.perform(get("/geografia/comunas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Providencia"));
    }

    @Test
    void obtenerComunaPorId_returnsOk() throws Exception {
        ComunaDTO c = new ComunaDTO(2L, "Viña del Mar", 2L, 2L);
        when(comunaService.obtenerComunaPorId(anyLong())).thenReturn(c);

        mockMvc.perform(get("/geografia/comunas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Viña del Mar"));
    }

    @Test
    void obtenerComunasPorRegion_returnsOk() throws Exception {
        ComunaDTO c = new ComunaDTO(3L, "Concepción Centro", 3L, 3L);
        when(comunaService.obtenerComunasPorRegion(3L)).thenReturn(List.of(c));

        mockMvc.perform(get("/geografia/comunas/region/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].regionId").value(3));
    }
}
