package com.example.ventasservice.controller;

import com.example.ventasservice.dto.*;
import com.example.ventasservice.service.BoletaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoletaController.class)
public class BoletaControllerTest {

    @MockBean
    private BoletaService boletaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarTodas_returnsOKAndJson() throws Exception {
        BoletaDTO boleta = new BoletaDTO();
        boleta.setId(1L);
        boleta.setClienteId(100L);
        boleta.setTotal(50000);
        boleta.setEstado("pendiente");
        boleta.setMetodoPago("efectivo");
        boleta.setFechaVenta(LocalDateTime.now());

        when(boletaService.obtenerTodasLasBoletas()).thenReturn(List.of(boleta));

        mockMvc.perform(get("/api/ventas/boletas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(100))
                .andExpect(jsonPath("$[0].total").value(50000))
                .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }

    @Test
    void obtenerPorId_returnsOK() throws Exception {
        BoletaDTO boleta = new BoletaDTO();
        boleta.setId(1L);
        boleta.setClienteId(100L);
        boleta.setTotal(50000);
        boleta.setEstado("confirmada");
        boleta.setMetodoPago("tarjeta");

        when(boletaService.obtenerBoletaPorId(1L)).thenReturn(Optional.of(boleta));

        mockMvc.perform(get("/api/ventas/boletas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(100))
                .andExpect(jsonPath("$.estado").value("confirmada"));
    }

    @Test
    void obtenerPorId_notFound() throws Exception {
        when(boletaService.obtenerBoletaPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ventas/boletas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorCliente_returnsOK() throws Exception {
        BoletaDTO boleta1 = new BoletaDTO();
        boleta1.setId(1L);
        boleta1.setClienteId(100L);
        boleta1.setTotal(50000);

        BoletaDTO boleta2 = new BoletaDTO();
        boleta2.setId(2L);
        boleta2.setClienteId(100L);
        boleta2.setTotal(75000);

        when(boletaService.obtenerBoletasPorCliente(100L)).thenReturn(Arrays.asList(boleta1, boleta2));

        mockMvc.perform(get("/api/ventas/boletas/cliente/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value(100))
                .andExpect(jsonPath("$[1].clienteId").value(100))
                .andExpect(jsonPath("$[0].total").value(50000))
                .andExpect(jsonPath("$[1].total").value(75000));
    }

    @Test
    void obtenerDetalles_returnsOK() throws Exception {
        DetalleBoletaDTO detalle1 = new DetalleBoletaDTO();
        detalle1.setId(1L);
        detalle1.setBoletaId(1L);
        detalle1.setNombreProducto("Zapatilla Nike");
        detalle1.setTallaId(42L);
        detalle1.setCantidad(2);
        detalle1.setPrecioUnitario(25000);
        detalle1.setSubtotal(50000);

        when(boletaService.obtenerDetallesPorBoleta(1L)).thenReturn(List.of(detalle1));

        mockMvc.perform(get("/api/ventas/boletas/1/detalles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Zapatilla Nike"))
                .andExpect(jsonPath("$[0].tallaId").value(42))
                .andExpect(jsonPath("$[0].cantidad").value(2))
                .andExpect(jsonPath("$[0].subtotal").value(50000));
    }

    @Test
    void crear_returnsCreated() throws Exception {
        BoletaDTO boletaCreada = new BoletaDTO();
        boletaCreada.setId(1L);
        boletaCreada.setClienteId(100L);
        boletaCreada.setTotal(50000);
        boletaCreada.setEstado("pendiente");
        boletaCreada.setMetodoPago("efectivo");
        boletaCreada.setFechaVenta(LocalDateTime.now());

        when(boletaService.crearBoleta(any(CrearBoletaRequest.class))).thenReturn(boletaCreada);

        String requestBody = """
                {
                    "clienteId": 100,
                    "metodoPago": "efectivo",
                    "observaciones": "Entrega rápida",
                    "detalles": [
                        {
                            "inventarioId": 1,
                            "nombreProducto": "Zapatilla Nike",
                            "tallaId": 42,
                            "cantidad": 2,
                            "precioUnitario": 25000
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/api/ventas/boletas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(100))
                .andExpect(jsonPath("$.total").value(50000))
                .andExpect(jsonPath("$.estado").value("pendiente"));
    }

    @Test
    void cambiarEstado_returnsOK() throws Exception {
        BoletaDTO boletaActualizada = new BoletaDTO();
        boletaActualizada.setId(1L);
        boletaActualizada.setClienteId(100L);
        boletaActualizada.setEstado("confirmada");

        when(boletaService.cambiarEstado(1L, "confirmada")).thenReturn(Optional.of(boletaActualizada));

        mockMvc.perform(put("/api/ventas/boletas/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nuevoEstado\": \"confirmada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("confirmada"));
    }

    @Test
    void cambiarEstado_notFound() throws Exception {
        when(boletaService.cambiarEstado(999L, "confirmada")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/ventas/boletas/999/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nuevoEstado\": \"confirmada\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorCliente_listaVacia_returnsOK() throws Exception {
        when(boletaService.obtenerBoletasPorCliente(999L)).thenReturn(List.of());

        mockMvc.perform(get("/api/ventas/boletas/cliente/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void obtenerDetalles_listaVacia_returnsOK() throws Exception {
        when(boletaService.obtenerDetallesPorBoleta(999L)).thenReturn(List.of());

        mockMvc.perform(get("/api/ventas/boletas/999/detalles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
