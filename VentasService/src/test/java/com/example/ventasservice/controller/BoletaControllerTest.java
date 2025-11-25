package com.example.VentasService.controller;

import com.example.VentasService.dto.BoletaDTO;
import com.example.VentasService.dto.CambiarEstadoRequest;
import com.example.VentasService.dto.CrearBoletaRequest;
import com.example.VentasService.dto.DetalleBoletaDTO;
import com.example.VentasService.service.BoletaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoletaController.class)
public class BoletaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoletaService boletaService;

    @Autowired
    private ObjectMapper objectMapper;

    private BoletaDTO boletaDTO;
    private DetalleBoletaDTO detalleDTO;

    @BeforeEach
    void setUp() {
        // Preparar datos comunes
        detalleDTO = new DetalleBoletaDTO();
        detalleDTO.setId(10L);
        detalleDTO.setInventarioId(50L);
        detalleDTO.setNombreProducto("Zapatillas Nike");
        detalleDTO.setCantidad(1);
        detalleDTO.setPrecioUnitario(50000);
        detalleDTO.setSubtotal(50000);

        boletaDTO = new BoletaDTO();
        boletaDTO.setId(1L);
        boletaDTO.setClienteId(100L);
        boletaDTO.setTotal(50000);
        boletaDTO.setEstado("pendiente");
        boletaDTO.setMetodoPago("TARJETA");
        boletaDTO.setFechaVenta(LocalDateTime.now());
        boletaDTO.setDetalles(Arrays.asList(detalleDTO));
    }

    @Test
    void listarTodas_Ok() throws Exception {
        // Arrange
        List<BoletaDTO> lista = Arrays.asList(boletaDTO);
        when(boletaService.obtenerTodasLasBoletas()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/ventas/boletas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].clienteId", is(100)));
    }

    @Test
    void obtenerPorId_Ok() throws Exception {
        // Arrange
        when(boletaService.obtenerBoletaPorId(1L)).thenReturn(Optional.of(boletaDTO));

        // Act & Assert
        mockMvc.perform(get("/ventas/boletas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.total", is(50000)));
    }

    @Test
    void obtenerPorCliente_Ok() throws Exception {
        // Arrange
        List<BoletaDTO> lista = Arrays.asList(boletaDTO);
        when(boletaService.obtenerBoletasPorCliente(100L)).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/ventas/boletas/cliente/{idCliente}", 100L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clienteId", is(100)));
    }

    @Test
    void obtenerDetalles_Ok() throws Exception {
        // Arrange
        List<DetalleBoletaDTO> detalles = Arrays.asList(detalleDTO);
        when(boletaService.obtenerDetallesPorBoleta(1L)).thenReturn(detalles);

        // Act & Assert
        mockMvc.perform(get("/ventas/boletas/{id}/detalles", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProducto", is("Zapatillas Nike")));
    }

    @Test
    void crear_Ok() throws Exception {
        // Arrange
        CrearBoletaRequest request = new CrearBoletaRequest();
        request.setClienteId(100L);
        request.setMetodoPago("TARJETA");
        request.setDetalles(Arrays.asList(detalleDTO));

        when(boletaService.crearBoleta(any(CrearBoletaRequest.class))).thenReturn(boletaDTO);

        // Act & Assert
        mockMvc.perform(post("/ventas/boletas/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Convertir objeto a JSON string
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("pendiente")));
    }

    @Test
    void cambiarEstado_Ok() throws Exception {
        // Arrange
        CambiarEstadoRequest request = new CambiarEstadoRequest();
        request.setNuevoEstado("completada");

        BoletaDTO boletaActualizada = new BoletaDTO();
        boletaActualizada.setId(1L);
        boletaActualizada.setEstado("completada");

        when(boletaService.cambiarEstado(eq(1L), eq("completada")))
                .thenReturn(Optional.of(boletaActualizada));

        // Act & Assert
        mockMvc.perform(put("/ventas/boletas/{id}/estado", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("completada")));
    }
}
