package com.example.inventarioservice.controller;

import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
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

@WebMvcTest(controllers = ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioRepository inventarioRepository;

    @MockBean
    private MovimientoInventarioRepository movimientoRepository;

    @Test
    void estadisticasGenerales_returnsOk() throws Exception {
        Inventario i = new Inventario();
        i.setId(1L);
        i.setCantidad(10);
        i.setStockMinimo(5);

        when(inventarioRepository.findAll()).thenReturn(List.of(i));
        when(movimientoRepository.count()).thenReturn(2L);

        mockMvc.perform(get("/reportes/estadisticas-generales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProductos").value(1))
                .andExpect(jsonPath("$.totalMovimientos").value(2));
    }
}
