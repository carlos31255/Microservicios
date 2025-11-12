package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.service.RegionService;
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

@WebMvcTest(RegionController.class)
public class RegionControllerTest {

    @MockBean
    private RegionService regionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodasLasRegiones_returnsOKAndJson() throws Exception {
        RegionDTO region = new RegionDTO();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");
        region.setOrden(13);

        when(regionService.obtenerTodasLasRegiones()).thenReturn(List.of(region));

        mockMvc.perform(get("/api/geografia/regiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Región Metropolitana"))
                .andExpect(jsonPath("$[0].codigo").value("RM"));
    }

    @Test
    void obtenerRegionPorId_returnsOK() throws Exception {
        RegionDTO region = new RegionDTO();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");

        when(regionService.obtenerRegionPorId(1L)).thenReturn(region);

        mockMvc.perform(get("/api/geografia/regiones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Región Metropolitana"));
    }

    @Test
    void obtenerRegionPorCodigo_returnsOK() throws Exception {
        RegionDTO region = new RegionDTO();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");

        when(regionService.obtenerRegionPorCodigo("RM")).thenReturn(region);

        mockMvc.perform(get("/api/geografia/regiones/codigo/RM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("RM"))
                .andExpect(jsonPath("$.nombre").value("Región Metropolitana"));
    }

    @Test
    void guardarRegion_returnsCreated() throws Exception {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");
        region.setOrden(13);

        when(regionService.guardarRegion(any(Region.class))).thenReturn(region);

        mockMvc.perform(post("/api/geografia/regiones/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Región Metropolitana\", \"codigo\": \"RM\", \"orden\": 13}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Región Metropolitana"));
    }

    @Test
    void modificarRegion_returnsOK() throws Exception {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Región Metropolitana Actualizada");
        region.setCodigo("RM");

        when(regionService.modificarRegion(eq(1L), any(Region.class))).thenReturn(region);

        mockMvc.perform(put("/api/geografia/regiones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Región Metropolitana Actualizada\", \"codigo\": \"RM\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Región Metropolitana Actualizada"));
    }

    @Test
    void eliminarRegion_returnsNoContent() throws Exception {
        doNothing().when(regionService).eliminarRegion(1L);

        mockMvc.perform(delete("/api/geografia/regiones/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
