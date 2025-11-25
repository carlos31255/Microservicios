package com.example.entregasService.controller;

import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.service.EntregaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EntregaController.class)
public class EntregaControllerTest {

    @MockBean
    private EntregaService entregaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarTodas_returnsOKAndJson() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setIdBoleta(100L);
        entrega.setIdTransportista(5L);
        entrega.setEstadoEntrega("PENDIENTE");
        entrega.setDireccionEntrega("Av. Principal 123");
        entrega.setIdComuna(101L);

        when(entregaService.obtenerTodasLasEntregas()).thenReturn(List.of(entrega));

        mockMvc.perform(get("/entregas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEntrega").value(1))
                .andExpect(jsonPath("$[0].idBoleta").value(100))
                .andExpect(jsonPath("$[0].estadoEntrega").value("PENDIENTE"))
                .andExpect(jsonPath("$[0].direccionEntrega").value("Av. Principal 123"));
    }

    @Test
    void obtenerPorId_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setIdBoleta(100L);
        entrega.setEstadoEntrega("EN_CAMINO");
        entrega.setDireccionEntrega("Calle Falsa 456");
        entrega.setNombreCliente("Juan Pérez");
        entrega.setTelefonoCliente("912345678");

        when(entregaService.obtenerEntregaPorId(1L)).thenReturn(entrega);

        mockMvc.perform(get("/entregas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntrega").value(1))
                .andExpect(jsonPath("$.estadoEntrega").value("EN_CAMINO"))
                .andExpect(jsonPath("$.nombreCliente").value("Juan Pérez"));
    }

    @Test
    void listarPorTransportista_returnsOK() throws Exception {
        EntregaDTO entrega1 = new EntregaDTO();
        entrega1.setIdEntrega(1L);
        entrega1.setIdTransportista(5L);
        entrega1.setEstadoEntrega("EN_CAMINO");

        EntregaDTO entrega2 = new EntregaDTO();
        entrega2.setIdEntrega(2L);
        entrega2.setIdTransportista(5L);
        entrega2.setEstadoEntrega("PENDIENTE");

        when(entregaService.getEntregasByTransportista(5L)).thenReturn(List.of(entrega1, entrega2));

        mockMvc.perform(get("/entregas/transportista/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTransportista").value(5))
                .andExpect(jsonPath("$[1].idTransportista").value(5))
                .andExpect(jsonPath("$[0].estadoEntrega").value("EN_CAMINO"))
                .andExpect(jsonPath("$[1].estadoEntrega").value("PENDIENTE"));
    }

    @Test
    void listarPorEstado_returnsOK() throws Exception {
        EntregaDTO entrega1 = new EntregaDTO();
        entrega1.setIdEntrega(1L);
        entrega1.setEstadoEntrega("ENTREGADO");
        entrega1.setFechaEntrega(LocalDateTime.now());

        EntregaDTO entrega2 = new EntregaDTO();
        entrega2.setIdEntrega(2L);
        entrega2.setEstadoEntrega("ENTREGADO");
        entrega2.setFechaEntrega(LocalDateTime.now());

        when(entregaService.getEntregasByEstado("ENTREGADO")).thenReturn(List.of(entrega1, entrega2));

        mockMvc.perform(get("/entregas/estado/ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoEntrega").value("ENTREGADO"))
                .andExpect(jsonPath("$[1].estadoEntrega").value("ENTREGADO"));
    }

    @Test
    void asignarTransportista_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setIdBoleta(100L);
        entrega.setIdTransportista(5L);
        entrega.setEstadoEntrega("ASIGNADO");
        entrega.setFechaAsignacion(LocalDateTime.now());

        when(entregaService.asignarTransportista(1L, 5L)).thenReturn(entrega);

        mockMvc.perform(put("/entregas/1/asignar")
                        .param("transportistaId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntrega").value(1))
                .andExpect(jsonPath("$.idTransportista").value(5))
                .andExpect(jsonPath("$.estadoEntrega").value("ASIGNADO"));
    }

    @Test
    void completarEntrega_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setEstadoEntrega("ENTREGADO");
        entrega.setFechaEntrega(LocalDateTime.now());
        entrega.setObservacion("Entrega realizada sin problemas");

        when(entregaService.completarEntrega(eq(1L), anyString())).thenReturn(entrega);

        mockMvc.perform(put("/entregas/1/completar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Entrega realizada sin problemas\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntrega").value(1))
                .andExpect(jsonPath("$.estadoEntrega").value("ENTREGADO"))
                .andExpect(jsonPath("$.observacion").value("Entrega realizada sin problemas"));
    }

    @Test
    void completarEntrega_sinObservacion_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setEstadoEntrega("ENTREGADO");
        entrega.setFechaEntrega(LocalDateTime.now());

        when(entregaService.completarEntrega(eq(1L), isNull())).thenReturn(entrega);

        mockMvc.perform(put("/entregas/1/completar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntrega").value(1))
                .andExpect(jsonPath("$.estadoEntrega").value("ENTREGADO"));
    }

    @Test
    void cambiarEstado_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(1L);
        entrega.setEstadoEntrega("EN_CAMINO");

        when(entregaService.cambiarEstado(1L, "EN_CAMINO")).thenReturn(entrega);

        mockMvc.perform(put("/entregas/1/estado")
                        .param("nuevoEstado", "EN_CAMINO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEntrega").value(1))
                .andExpect(jsonPath("$.estadoEntrega").value("EN_CAMINO"));
    }

    @Test
    void listarPorEstado_conEstadoPendiente_returnsOK() throws Exception {
        EntregaDTO entrega = new EntregaDTO();
        entrega.setIdEntrega(3L);
        entrega.setEstadoEntrega("PENDIENTE");
        entrega.setIdBoleta(200L);

        when(entregaService.getEntregasByEstado("PENDIENTE")).thenReturn(List.of(entrega));

        mockMvc.perform(get("/entregas/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEntrega").value(3))
                .andExpect(jsonPath("$[0].estadoEntrega").value("PENDIENTE"))
                .andExpect(jsonPath("$[0].idBoleta").value(200));
    }

    @Test
    void listarPorTransportista_sinEntregas_returnsEmptyList() throws Exception {
        when(entregaService.getEntregasByTransportista(99L)).thenReturn(List.of());

        mockMvc.perform(get("/entregas/transportista/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
