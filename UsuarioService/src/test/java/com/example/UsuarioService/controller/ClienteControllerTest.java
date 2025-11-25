package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.ClienteDTO;
import com.example.UsuarioService.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodosLosClientes_returnsOKAndJson() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");
        cliente.setNombreCompleto("Juan Pérez");
        cliente.setEmail("juan@email.cl");
        cliente.setTelefono("+56911111111");

        when(clienteService.obtenerTodosLosClientes()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersona").value(1))
                .andExpect(jsonPath("$[0].categoria").value("VIP"))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Juan Pérez"));
    }

    @Test
    void obtenerClientePorId_returnsOK() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");
        cliente.setNombreCompleto("Juan Pérez");

        when(clienteService.obtenerClientePorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.categoria").value("VIP"));
    }

    @Test
    void crearCliente_returnsCreated() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setIdPersona(1L);
        cliente.setCategoria("premium");
        cliente.setNombreCompleto("María González");

        when(clienteService.crearCliente(any(ClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idPersona\": 1, \"categoria\": \"premium\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.categoria").value("premium"));
    }

    @Test
    void actualizarCategoriaCliente_returnsOK() throws Exception {
        ClienteDTO clienteActualizado = new ClienteDTO();
        clienteActualizado.setIdPersona(1L);
        clienteActualizado.setCategoria("VIP");

        when(clienteService.actualizarCategoria(eq(1L), eq("VIP"))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/clientes/1/categoria")
                        .param("nuevaCategoria", "VIP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria").value("VIP"));
    }

    @Test
    void eliminarCliente_returnsNoContent() throws Exception {
        when(clienteService.eliminarCliente(1L)).thenReturn(true);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void filtrarPorCategoria_returnsOK() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");

        when(clienteService.obtenerClientesPorCategoria("VIP")).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes/categoria/VIP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria").value("VIP"));
    }
}
