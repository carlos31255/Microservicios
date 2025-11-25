package com.example.UsuarioService.controller;

import com.example.UsuarioService.repository.ClienteRepository;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaRepository personaRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void estadisticasGenerales_returnsOk() throws Exception {
        when(personaRepository.count()).thenReturn(5L);
        when(clienteRepository.count()).thenReturn(2L);
        when(usuarioRepository.count()).thenReturn(3L);
        when(personaRepository.countByEstado("activo")).thenReturn(4L);
        when(personaRepository.countByEstado("inactivo")).thenReturn(1L);
        when(clienteRepository.countByActivoTrue()).thenReturn(2L);
        when(clienteRepository.countByActivoFalse()).thenReturn(0L);
        when(usuarioRepository.countByActivoTrue()).thenReturn(3L);
        when(usuarioRepository.countByActivoFalse()).thenReturn(0L);

        mockMvc.perform(get("/reportes/estadisticas-generales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPersonas").value(5))
                .andExpect(jsonPath("$.totalClientes").value(2));
    }

    @Test
    void distribucionPersonas_porEstado_returnsOk() throws Exception {
        when(personaRepository.countByEstado("activo")).thenReturn(7L);
        when(personaRepository.countByEstado("inactivo")).thenReturn(2L);

        mockMvc.perform(get("/reportes/personas/por-estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activas").value(7))
                .andExpect(jsonPath("$.inactivas").value(2));
    }
}
