package com.example.ventasservice.controller;

import com.example.ventasservice.model.CartItem;
import com.example.ventasservice.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    void testGetCart() throws Exception {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setClienteId(100L);
        item.setModeloId(10L);
        item.setTallaId(42L);
        item.setCantidad(2);
        item.setPrecioUnitario(25000);
        item.setNombreProducto("Zapatilla Nike");

        when(cartService.getCartForCliente(100L)).thenReturn(Arrays.asList(item));

        mockMvc.perform(get("/api/carrito/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreProducto").value("Zapatilla Nike"));

        verify(cartService).getCartForCliente(100L);
    }

    @Test
    void testAddOrUpdate() throws Exception {
        // request body
        var req = new java.util.HashMap<String, Object>();
        req.put("clienteId", 100);
        req.put("modeloId", 10);
        req.put("tallaId", 42);
        req.put("cantidad", 2);
        req.put("precioUnitario", 25000);
        req.put("nombreProducto", "Zapatilla Nike");

        when(cartService.addOrUpdate(ArgumentMatchers.any(), ArgumentMatchers.isNull())).thenReturn(7L);

        mockMvc.perform(post("/api/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("7"));

                verify(cartService).addOrUpdate(ArgumentMatchers.any(), ArgumentMatchers.isNull());
    }

        @Test
        void testAddOrUpdate_withTallaString() throws Exception {
                var req = new java.util.HashMap<String, Object>();
                req.put("clienteId", 100);
                req.put("modeloId", 10);
                req.put("talla", "42");
                req.put("cantidad", 2);
                req.put("precioUnitario", 25000);
                req.put("nombreProducto", "Zapatilla Nike");

                when(cartService.addOrUpdate(ArgumentMatchers.any(), eq("42"))).thenReturn(8L);

                mockMvc.perform(post("/api/carrito")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isCreated())
                                .andExpect(content().string("8"));

                verify(cartService).addOrUpdate(ArgumentMatchers.any(), eq("42"));
        }

    @Test
    void testCount() throws Exception {
        when(cartService.getCountByCliente(100L)).thenReturn(3);

        mockMvc.perform(get("/api/carrito/100/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(cartService).getCountByCliente(100L);
    }

    @Test
    void testDeleteItemAndClear() throws Exception {
        // delete item
        mockMvc.perform(delete("/api/carrito/100/items/9"))
                .andExpect(status().isNoContent());

        verify(cartService).delete(argThat(i -> i != null && i.getId() != null && i.getId().equals(9L)));

        // clear
        mockMvc.perform(delete("/api/carrito/100"))
                .andExpect(status().isNoContent());

        verify(cartService).clear(100L);
    }

    @Test
    void testGetItem_foundAndNotFound() throws Exception {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setClienteId(100L);
        item.setModeloId(10L);
        item.setTallaId(42L);
        item.setCantidad(2);
        item.setNombreProducto("Zapatilla Nike");

        when(cartService.getItem(100L, 10L, 42L, null)).thenReturn(item);

        mockMvc.perform(get("/api/carrito/100/item")
                        .param("modeloId", "10")
                                .param("tallaId", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Zapatilla Nike"));

        // not found
        when(cartService.getItem(200L, 1L, 39L, null)).thenReturn(null);

        mockMvc.perform(get("/api/carrito/200/item")
                        .param("modeloId", "1")
                        .param("tallaId", "39"))
                .andExpect(status().isNotFound());
    }
}
