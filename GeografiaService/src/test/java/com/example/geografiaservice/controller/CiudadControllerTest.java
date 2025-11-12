package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.service.CiudadService;
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

@WebMvcTest(CiudadController.class)
public class CiudadControllerTest {

    @MockBean
    private CiudadService ciudadService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodasLasCiudades_returnsOKAndJson() throws Exception {
        CiudadDTO ciudad = new CiudadDTO();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegionId(13L);

        when(ciudadService.obtenerTodasLasCiudades()).thenReturn(List.of(ciudad));

        mockMvc.perform(get("/api/geografia/ciudades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Santiago"))
                .andExpect(jsonPath("$[0].regionId").value(13));
    }

    @Test
    void obtenerCiudadPorId_returnsOK() throws Exception {
        CiudadDTO ciudad = new CiudadDTO();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegionId(13L);

        when(ciudadService.obtenerCiudadPorId(1L)).thenReturn(ciudad);

        mockMvc.perform(get("/api/geografia/ciudades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Santiago"));
    }

    @Test
    void obtenerCiudadesPorRegion_returnsOK() throws Exception {
        CiudadDTO ciudad = new CiudadDTO();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegionId(13L);

        when(ciudadService.obtenerCiudadesPorRegion(13L)).thenReturn(List.of(ciudad));

        mockMvc.perform(get("/api/geografia/ciudades/region/13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].regionId").value(13))
                .andExpect(jsonPath("$[0].nombre").value("Santiago"));
    }

    @Test
    void guardarCiudad_returnsCreated() throws Exception {
        Region region = new Region();
        region.setId(13L);
        
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);

        when(ciudadService.guardarCiudad(any(Ciudad.class))).thenReturn(ciudad);

        mockMvc.perform(post("/api/geografia/ciudades/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Santiago\", \"regionId\": 13}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Santiago"));
    }

    @Test
    void modificarCiudad_returnsOK() throws Exception {
        Region region = new Region();
        region.setId(13L);
        
        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago Centro");
        ciudad.setRegion(region);

        when(ciudadService.modificarCiudad(eq(1L), any(Ciudad.class))).thenReturn(ciudad);

        mockMvc.perform(put("/api/geografia/ciudades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Santiago Centro\", \"regionId\": 13}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Santiago Centro"));
    }

    @Test
    void eliminarCiudad_returnsNoContent() throws Exception {
        doNothing().when(ciudadService).eliminarCiudad(1L);

        mockMvc.perform(delete("/api/geografia/ciudades/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
