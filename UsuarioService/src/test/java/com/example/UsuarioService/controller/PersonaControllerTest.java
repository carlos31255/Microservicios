package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.service.PersonaService;
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

@WebMvcTest(PersonaController.class)
public class PersonaControllerTest {

    @MockBean
    private PersonaService personaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void obtenerTodasLasPersonas_returnsOKAndJson() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");
        persona.setEmail("juan@email.cl");

        when(personaService.obtenerTodasLasPersonas()).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersona").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"));
    }

    @Test
    void obtenerPersonaPorId_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        when(personaService.obtenerPersonaPorId(1L)).thenReturn(persona);

        mockMvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerPersonaPorRut_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setRut("12345678-9");

        when(personaService.obtenerPersonaPorRut("12345678-9")).thenReturn(persona);

        mockMvc.perform(get("/api/personas/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void obtenerPersonaPorUsername_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setUsername("juan@email.cl");

        when(personaService.obtenerPersonaPorUsername("juan@email.cl")).thenReturn(persona);

        mockMvc.perform(get("/api/personas/username/juan@email.cl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan@email.cl"));
    }

    @Test
    void crearPersona_returnsCreated() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setNombre("María");
        persona.setApellido("González");

        when(personaService.crearPersona(any(PersonaDTO.class))).thenReturn(persona);

        mockMvc.perform(post("/api/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"María\", \"apellido\": \"González\", \"rut\": \"11111111-1\", \"email\": \"maria@email.cl\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.nombre").value("María"));
    }

    @Test
    void actualizarPersona_returnsOK() throws Exception {
        PersonaDTO personaActualizada = new PersonaDTO();
        personaActualizada.setIdPersona(1L);
        personaActualizada.setNombre("Juan Modificado");

        when(personaService.actualizarPersona(eq(1L), any(PersonaDTO.class))).thenReturn(personaActualizada);

        mockMvc.perform(put("/api/personas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Juan Modificado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Modificado"));
    }

    @Test
    void eliminarPersona_returnsNoContent() throws Exception {
        when(personaService.eliminarPersona(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/personas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorNombre_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");

        when(personaService.buscarPersonasPorNombre("Juan")).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas/buscar").param("nombre", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void filtrarPorEstado_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setEstado("activo");

        when(personaService.obtenerPersonasPorEstado("activo")).thenReturn(List.of(persona));

        mockMvc.perform(get("/api/personas/estado/activo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("activo"));
    }

    @Test
    void verificarCredenciales_returnsOK() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setUsername("juan@email.cl");

        when(personaService.verificarCredenciales("juan@email.cl", "password123")).thenReturn(persona);

        mockMvc.perform(post("/api/personas/verificar-credenciales")
                .param("username", "juan@email.cl")
                .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan@email.cl"));
    }
}
