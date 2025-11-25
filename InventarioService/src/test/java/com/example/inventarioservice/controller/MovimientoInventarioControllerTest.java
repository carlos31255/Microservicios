package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.service.MovimientoInventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovimientoInventarioController.class)
public class MovimientoInventarioControllerTest {

    @MockBean
    private MovimientoInventarioService movimientoInventarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodosLosMovimientos_returnsOKAndJson() throws Exception {
        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();
        movimiento.setId(1L);
        movimiento.setInventarioId(10L);
        movimiento.setTipo("entrada");
        movimiento.setCantidad(50);
        movimiento.setMotivo("Stock inicial");
        movimiento.setUsuarioId(1L);

        when(movimientoInventarioService.obtenerTodosLosMovimientos()).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/inventario/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipo").value("entrada"))
                .andExpect(jsonPath("$[0].cantidad").value(50));
    }

    @Test
    void obtenerMovimientoPorId_returnsOK() throws Exception {
        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();
        movimiento.setId(1L);
        movimiento.setTipo("entrada");
        movimiento.setCantidad(50);

        when(movimientoInventarioService.obtenerMovimientoPorId(1L)).thenReturn(movimiento);

        mockMvc.perform(get("/inventario/movimientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("entrada"));
    }

    @Test
    void obtenerMovimientosPorInventarioId_returnsOK() throws Exception {
        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();
        movimiento.setId(1L);
        movimiento.setInventarioId(10L);
        movimiento.setTipo("entrada");

        when(movimientoInventarioService.obtenerMovimientosPorInventarioId(10L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/inventario/movimientos/inventario/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].inventarioId").value(10));
    }

    @Test
    void obtenerMovimientosPorTipo_returnsOK() throws Exception {
        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();
        movimiento.setId(1L);
        movimiento.setTipo("salida");
        movimiento.setCantidad(20);

        when(movimientoInventarioService.obtenerMovimientosPorTipo("salida")).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/inventario/movimientos/tipo/salida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("salida"))
                .andExpect(jsonPath("$[0].cantidad").value(20));
    }

    @Test
    void obtenerMovimientosPorUsuarioId_returnsOK() throws Exception {
        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();
        movimiento.setId(1L);
        movimiento.setUsuarioId(5L);
        movimiento.setTipo("entrada");

        when(movimientoInventarioService.obtenerMovimientosPorUsuarioId(5L)).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/inventario/movimientos/usuario/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value(5));
    }
}
