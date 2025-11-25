package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.RolRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.service.PersonaService;
import com.example.UsuarioService.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private RolRepository rolRepository;

    @MockBean
    private PersonaRepository personaRepository;

    @Test
    void login_validCredentials_returnsOk() throws Exception {
        PersonaDTO persona = new PersonaDTO();
        persona.setIdPersona(1L);
        persona.setUsername("juan@email.cl");
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        when(personaService.verificarCredenciales("juan@email.cl", "secret"))
                .thenReturn(persona);

        when(usuarioRepository.findByIdPersonaWithPersonaAndRol(1L)).thenReturn(Optional.empty());

        String body = "{\"username\":\"juan@email.cl\",\"password\":\"secret\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersona").value(1))
                .andExpect(jsonPath("$.username").value("juan@email.cl"));
    }
}
