package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.RolDTO;
import com.example.UsuarioService.service.RolService;
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

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @MockBean
    private RolService rolService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodosLosRoles_returnsOKAndJson() throws Exception {
        RolDTO rol = new RolDTO();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");
        rol.setDescripcion("Rol de administrador");

        when(rolService.obtenerTodosLosRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idRol").value(1))
                .andExpect(jsonPath("$[0].nombreRol").value("Administrador"))
                .andExpect(jsonPath("$[0].descripcion").value("Rol de administrador"));
    }

    @Test
    void obtenerRolPorId_returnsOK() throws Exception {
        RolDTO rol = new RolDTO();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");

        when(rolService.obtenerRolPorId(1L)).thenReturn(rol);

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRol").value(1))
                .andExpect(jsonPath("$.nombreRol").value("Administrador"));
    }

    @Test
    void obtenerRolPorNombre_returnsOK() throws Exception {
        RolDTO rol = new RolDTO();
        rol.setIdRol(2L);
        rol.setNombreRol("Transportista");

        when(rolService.obtenerRolPorNombre("Transportista")).thenReturn(rol);

        mockMvc.perform(get("/roles/nombre/Transportista"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRol").value(2))
                .andExpect(jsonPath("$.nombreRol").value("Transportista"));
    }

    @Test
    void crearRol_returnsCreated() throws Exception {
        RolDTO rol = new RolDTO();
        rol.setIdRol(1L);
        rol.setNombreRol("Nuevo Rol");
        rol.setDescripcion("Descripción del rol");

        when(rolService.crearRol(any(RolDTO.class))).thenReturn(rol);

        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombreRol\": \"Nuevo Rol\", \"descripcion\": \"Descripción del rol\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRol").value(1))
                .andExpect(jsonPath("$.nombreRol").value("Nuevo Rol"));
    }

    @Test
    void actualizarRol_returnsOK() throws Exception {
        RolDTO rolActualizado = new RolDTO();
        rolActualizado.setIdRol(1L);
        rolActualizado.setNombreRol("Rol Modificado");

        when(rolService.actualizarRol(eq(1L), any(RolDTO.class))).thenReturn(rolActualizado);

        mockMvc.perform(put("/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombreRol\": \"Rol Modificado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("Rol Modificado"));
    }

    @Test
    void eliminarRol_returnsNoContent() throws Exception {
        when(rolService.eliminarRol(1L)).thenReturn(true);

        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isNoContent());
    }
}
