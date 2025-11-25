package com.example.inventarioservice.controller;

import com.example.inventarioservice.model.Marca;
import com.example.inventarioservice.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MarcaController.class)
public class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarcaRepository marcaRepository;

    @Test
    void list_returnsOk() throws Exception {
        Marca m = new Marca();
        m.setId(1L);
        m.setNombre("MarcaX");

        when(marcaRepository.findAll()).thenReturn(List.of(m));

        mockMvc.perform(get("/inventario/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getById_returnsOk() throws Exception {
        Marca m = new Marca();
        m.setId(2L);
        m.setNombre("MarcaY");

        when(marcaRepository.findById(2L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/inventario/marcas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}
