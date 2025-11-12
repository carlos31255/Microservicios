package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.ComunaDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.service.ComunaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComunaController.class)
public class ComunaControllerTest {

    @MockBean
    private ComunaService comunaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodasLasComunas_returnsOKAndJson() throws Exception {
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegionId(13L);
        comuna.setCiudadId(1L);

        when(comunaService.obtenerTodasLasComunas()).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/geografia/comunas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Providencia"))
                .andExpect(jsonPath("$[0].regionId").value(13));
    }

    @Test
    void obtenerComunaPorId_returnsOK() throws Exception {
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegionId(13L);
        comuna.setCiudadId(1L);

        when(comunaService.obtenerComunaPorId(1L)).thenReturn(comuna);

        mockMvc.perform(get("/api/geografia/comunas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Providencia"));
    }

    @Test
    void obtenerComunasPorRegion_returnsOK() throws Exception {
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegionId(13L);
        comuna.setCiudadId(1L);

        when(comunaService.obtenerComunasPorRegion(13L)).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/geografia/comunas/region/13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].regionId").value(13))
                .andExpect(jsonPath("$[0].nombre").value("Providencia"));
    }

    @Test
    void obtenerComunasPorCiudad_returnsOK() throws Exception {
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegionId(13L);
        comuna.setCiudadId(1L);

        when(comunaService.obtenerComunasPorCiudad(1L)).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/geografia/comunas/ciudad/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ciudadId").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Providencia"));
    }

    @Test
    void obtenerComunasPorRegionYCiudad_returnsOK() throws Exception {
        ComunaDTO comuna = new ComunaDTO();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegionId(13L);
        comuna.setCiudadId(1L);

        when(comunaService.obtenerComunasPorRegionYCiudad(13L, 1L)).thenReturn(List.of(comuna));

        mockMvc.perform(get("/api/geografia/comunas/region/13/ciudad/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].regionId").value(13))
                .andExpect(jsonPath("$[0].ciudadId").value(1));
    }

    @Test
    void guardarComuna_returnsCreated() throws Exception {
        Region region = new Region();
        region.setId(13L);
        
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        
        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaService.guardarComuna(any(Comuna.class))).thenReturn(comuna);

        mockMvc.perform(post("/api/geografia/comunas/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Providencia\", \"regionId\": 13, \"ciudadId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Providencia"));
    }

    @Test
    void modificarComuna_returnsOK() throws Exception {
        Region region = new Region();
        region.setId(13L);
        
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        
        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia Actualizada");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaService.modificarComuna(eq(1L), any(Comuna.class))).thenReturn(comuna);

        mockMvc.perform(put("/api/geografia/comunas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Providencia Actualizada\", \"regionId\": 13, \"ciudadId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Providencia Actualizada"));
    }

    @Test
    void eliminarComuna_returnsNoContent() throws Exception {
        doNothing().when(comunaService).eliminarComuna(1L);

        mockMvc.perform(delete("/api/geografia/comunas/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
