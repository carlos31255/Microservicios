package com.example.inventarioservice.controller;

import com.example.inventarioservice.model.Producto;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepository;

    @MockBean
    private TallaRepository tallaRepository;

    @Test
    void list_returnsOk() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Prod A");

        when(productoRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/inventario/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Prod A"));
    }

    @Test
    void getById_returnsOk_andTallas() throws Exception {
        Producto p = new Producto();
        p.setId(2L);
        p.setNombre("Prod B");
        Talla t = new Talla();
        t.setId(10L);
        t.setValor("M");
        p.setTallas(Set.of(t));

        when(productoRepository.findById(2L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/inventario/productos/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Prod B"));

        mockMvc.perform(get("/inventario/productos/2/tallas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }
}
