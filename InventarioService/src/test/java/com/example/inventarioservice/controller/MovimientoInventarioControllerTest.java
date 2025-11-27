package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.service.MovimientoInventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimientoInventarioController.class)
public class MovimientoInventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimientoInventarioService movimientoInventarioService;

    @Test
    void obtenerTodosLosMovimientos_DeberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(1L);
        dto.setTipo("ENTRADA");
        dto.setCantidad(10);
        
        when(movimientoInventarioService.obtenerTodosLosMovimientos()).thenReturn(Arrays.asList(dto));

        // Act & Assert
        mockMvc.perform(get("/inventario/movimientos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$[0].cantidad").value(10));
    }

    @Test
    void obtenerMovimientoPorId_CuandoExiste_DeberiaRetornarDtoYStatus200() throws Exception {
        // Arrange
        Long movimientoId = 1L;
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(movimientoId);
        dto.setTipo("SALIDA");

        when(movimientoInventarioService.obtenerMovimientoPorId(movimientoId)).thenReturn(dto);

        // Act & Assert
        // URL: /inventario/movimientos/{id}
        mockMvc.perform(get("/inventario/movimientos/{id}", movimientoId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimientoId))
                .andExpect(jsonPath("$.tipo").value("SALIDA"));
    }

    @Test
    void obtenerMovimientoPorId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        // Arrange
        Long movimientoId = 99L;
        when(movimientoInventarioService.obtenerMovimientoPorId(movimientoId))
                .thenThrow(new RuntimeException("Movimiento no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/inventario/movimientos/{id}", movimientoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerMovimientosPorInventarioId_DeberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long inventarioId = 10L;
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(2L);
        dto.setInventarioId(inventarioId);
        dto.setTipo("ENTRADA");

        when(movimientoInventarioService.obtenerMovimientosPorInventarioId(inventarioId))
                .thenReturn(Collections.singletonList(dto));

        // Act & Assert
        // URL: /inventario/movimientos/inventario/{inventarioId}
        mockMvc.perform(get("/inventario/movimientos/inventario/{inventarioId}", inventarioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inventarioId").value(inventarioId));
    }

    @Test
    void obtenerMovimientosPorTipo_DeberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        String tipo = "entrada";
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(3L);
        dto.setTipo(tipo);

        when(movimientoInventarioService.obtenerMovimientosPorTipo(tipo))
                .thenReturn(Collections.singletonList(dto));

        // Act & Assert
        // URL: /inventario/movimientos/tipo/{tipo}
        mockMvc.perform(get("/inventario/movimientos/tipo/{tipo}", tipo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value(tipo));
    }

    @Test
    void obtenerMovimientosPorUsuarioId_DeberiaRetornarListaYStatus200() throws Exception {
        // Arrange
        Long usuarioId = 5L;
        MovimientoInventarioDTO dto = new MovimientoInventarioDTO();
        dto.setId(4L);
        dto.setUsuarioId(usuarioId);

        when(movimientoInventarioService.obtenerMovimientosPorUsuarioId(usuarioId))
                .thenReturn(Collections.singletonList(dto));

        // Act & Assert
        // URL: /inventario/movimientos/usuario/{usuarioId}
        mockMvc.perform(get("/inventario/movimientos/usuario/{usuarioId}", usuarioId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(usuarioId));
    }
}
