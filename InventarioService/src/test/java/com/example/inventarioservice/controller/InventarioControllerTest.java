package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodoElInventario_returnsOKAndJson() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setTalla("42");
        inventario.setCantidad(50);
        inventario.setStockMinimo(10);

        when(inventarioService.obtenerTodoElInventario()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Zapatilla Nike"))
                .andExpect(jsonPath("$[0].cantidad").value(50));
    }

    @Test
    void obtenerInventarioPorId_returnsOK() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setCantidad(50);

        when(inventarioService.obtenerInventarioPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zapatilla Nike"));
    }

    @Test
    void obtenerInventarioPorProductoId_returnsOK() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setTalla("42");
        inventario.setCantidad(50);

        when(inventarioService.obtenerInventarioPorProductoId(100L)).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/producto/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(100))
                .andExpect(jsonPath("$[0].talla").value("42"));
    }

    @Test
    void obtenerInventarioPorProductoIdYTalla_returnsOK() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setTalla("42");
        inventario.setCantidad(50);

        when(inventarioService.obtenerInventarioPorProductoIdYTalla(100L, "42")).thenReturn(inventario);

        mockMvc.perform(get("/inventario/producto/100/talla/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value(100))
                .andExpect(jsonPath("$.talla").value("42"));
    }

    @Test
    void obtenerProductosConStockBajo_returnsOK() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setCantidad(5);
        inventario.setStockMinimo(10);

        when(inventarioService.obtenerProductosConStockBajo()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(5))
                .andExpect(jsonPath("$[0].stockMinimo").value(10));
    }

    @Test
    void buscarProductosPorNombre_returnsOK() throws Exception {
        InventarioDTO inventario = new InventarioDTO();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike");

        when(inventarioService.buscarProductosPorNombre("Nike")).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/buscar")
                        .param("nombre", "Nike"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Zapatilla Nike"));
    }

    @Test
    void crearInventario_returnsCreated() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setTalla("42");
        inventario.setCantidad(50);
        inventario.setStockMinimo(10);

        when(inventarioService.crearInventario(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/inventario/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\": 100, \"nombre\": \"Zapatilla Nike\", \"talla\": \"42\", \"cantidad\": 50, \"stockMinimo\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Zapatilla Nike"));
    }

    @Test
    void actualizarInventario_returnsOK() throws Exception {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike Actualizada");
        inventario.setCantidad(60);

        when(inventarioService.actualizarInventario(eq(1L), any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(put("/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Zapatilla Nike Actualizada\", \"cantidad\": 60}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Zapatilla Nike Actualizada"))
                .andExpect(jsonPath("$.cantidad").value(60));
    }

    @Test
    void verificarDisponibilidad_returnsOK() throws Exception {
        when(inventarioService.verificarDisponibilidad(100L, "42", 10)).thenReturn(true);

        mockMvc.perform(get("/inventario/verificar-disponibilidad")
                        .param("productoId", "100")
                        .param("talla", "42")
                        .param("cantidad", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void eliminarInventario_returnsNoContent() throws Exception {
        doNothing().when(inventarioService).eliminarInventario(1L);

        mockMvc.perform(delete("/inventario/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
