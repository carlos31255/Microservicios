package com.example.inventarioservice.controller;

import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.TallaRepository;
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

@WebMvcTest(controllers = TallaController.class)
public class TallaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TallaRepository tallaRepository;

    @Test
    void list_returnsOk() throws Exception {
        Talla t = new Talla();
        t.setId(1L);
        t.setValor("42");

        when(tallaRepository.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/inventario/tallas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getById_returnsOk() throws Exception {
        Talla t = new Talla();
        t.setId(2L);
        t.setValor("M");

        when(tallaRepository.findById(2L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/inventario/tallas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }
}
