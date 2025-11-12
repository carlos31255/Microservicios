package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.UsuarioDTO;
import com.example.UsuarioService.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodosLosUsuarios_returnsOKAndJson() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setNombreCompleto("Juan Pérez");
        usuario.setNombreRol("Administrador");

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersona").value(1))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].nombreRol").value("Administrador"));
    }

    @Test
    void obtenerUsuarioPorId_returnsOK() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdPersona(1L);
        usuario.setNombreCompleto("Juan Pérez");

        when(usuarioService.obtenerUsuarioPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"));
    }

    @Test
    void crearUsuario_returnsCreated() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdPersona(1L);
        usuario.setIdRol(2L);
        usuario.setNombreCompleto("María González");

        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idPersona\": 1, \"idRol\": 2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("María González"));
    }

    @Test
    void actualizarRolUsuario_returnsOK() throws Exception {
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setIdPersona(1L);
        usuarioActualizado.setIdRol(3L);
        usuarioActualizado.setNombreRol("Cliente");

        when(usuarioService.actualizarRolUsuario(eq(1L), eq(3L))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/1/rol")
                        .param("nuevoIdRol", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRol").value(3))
                .andExpect(jsonPath("$.nombreRol").value("Cliente"));
    }

    @Test
    void eliminarUsuario_returnsNoContent() throws Exception {
        when(usuarioService.eliminarUsuario(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerUsuariosPorRol_returnsOK() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setNombreRol("Administrador");

        when(usuarioService.obtenerUsuariosPorRol(1L)).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios/rol/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreRol").value("Administrador"));
    }
}
