package com.example.VentasService.controller;

import com.example.VentasService.model.CartItem;
import com.example.VentasService.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void getCart_returnsOk() throws Exception {
        CartItem item = new CartItem(1L, 5L, 100L, 10L, 2, 500, "Zapato A", LocalDateTime.now(), LocalDateTime.now());
        when(cartService.getCartForCliente(5L)).thenReturn(List.of(item));
        when(cartService.getTallaLabel(10L)).thenReturn("M");

        mockMvc.perform(get("/carrito/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreProducto").value("Zapato A"))
                .andExpect(jsonPath("$[0].talla").value("M"));
    }

    @Test
    void addOrUpdate_returnsCreated_andBodyList() throws Exception {
        // Prepare mock behavior
        CartItem returned = new CartItem(2L, 6L, 200L, 20L, 1, 1000, "Camisa B", LocalDateTime.now(), LocalDateTime.now());
        when(cartService.addOrUpdate(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString())).thenReturn(2L);
        when(cartService.getCartForCliente(6L)).thenReturn(List.of(returned));
        when(cartService.getTallaLabel(20L)).thenReturn("L");

        String json = "{\"clienteId\":6,\"modeloId\":200,\"tallaId\":20,\"cantidad\":1,\"precioUnitario\":1000,\"nombreProducto\":\"Camisa B\"}";

        mockMvc.perform(post("/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].nombreProducto").value("Camisa B"))
                .andExpect(jsonPath("$[0].talla").value("L"));
    }

    @Test
    void getItem_returnsOk() throws Exception {
        CartItem item = new CartItem(3L, 7L, 300L, 30L, 1, 700, "Polera C", LocalDateTime.now(), LocalDateTime.now());
        when(cartService.getItem(7L, 300L, 30L, null)).thenReturn(item);
        when(cartService.getTallaLabel(30L)).thenReturn("S");

        mockMvc.perform(get("/carrito/7/item?modeloId=300&tallaId=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombreProducto").value("Polera C"))
                .andExpect(jsonPath("$.talla").value("S"));
    }
}
