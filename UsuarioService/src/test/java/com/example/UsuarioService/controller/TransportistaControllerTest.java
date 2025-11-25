package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.TransportistaDTO;
import com.example.UsuarioService.service.TransportistaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransportistaController.class)
public class TransportistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransportistaService transportistaService;

    @Test
    void listarTransportistas_returnsOk() throws Exception {
        TransportistaDTO t = new TransportistaDTO();
        t.setIdTransportista(1L);
        t.setIdPersona(10L);
        t.setPatente("ABC-123");

        when(transportistaService.obtenerTodos()).thenReturn(List.of(t));

        mockMvc.perform(get("/transportistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransportista").value(1));
    }

    @Test
    void obtenerPorId_returnsOk() throws Exception {
        TransportistaDTO t = new TransportistaDTO();
        t.setIdTransportista(2L);
        t.setPatente("XYZ-999");

        when(transportistaService.obtenerPorId(2L)).thenReturn(t);

        mockMvc.perform(get("/transportistas/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransportista").value(2));
    }
}
