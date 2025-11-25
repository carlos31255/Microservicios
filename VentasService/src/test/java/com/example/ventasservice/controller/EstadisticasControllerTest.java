package com.example.VentasService.controller;

import com.example.VentasService.model.Boleta;
import com.example.VentasService.repository.BoletaRepository;
import com.example.VentasService.repository.DetalleBoletaRepository;
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

@WebMvcTest(controllers = EstadisticasController.class)
public class EstadisticasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoletaRepository boletaRepository;

    @MockBean
    private DetalleBoletaRepository detalleBoletaRepository;

    @Test
    void dashboard_returnsOk() throws Exception {
        Boleta b = new Boleta();
        b.setId(1L);
        b.setTotal(1500);

        when(boletaRepository.count()).thenReturn(1L);
        when(detalleBoletaRepository.count()).thenReturn(3L);
        when(boletaRepository.countByEstado("pendiente")).thenReturn(0L);
        when(boletaRepository.countByEstado("confirmada")).thenReturn(1L);
        when(boletaRepository.countByEstado("completada")).thenReturn(0L);
        when(boletaRepository.countByEstado("cancelada")).thenReturn(0L);
        when(boletaRepository.findAll()).thenReturn(List.of(b));

        mockMvc.perform(get("/ventas/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBoletas").value(1))
                .andExpect(jsonPath("$.totalVentas").value(1500));
    }
}
