package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.UsuarioDTO;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.model.Usuario;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.RolRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testObtenerTodosLosUsuarios() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");
        persona.setUsername("juan@email.cl");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");

        Usuario usuario = new Usuario();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setPersona(persona);
        usuario.setRol(rol);

        when(usuarioRepository.findAllWithPersonaAndRol()).thenReturn(Arrays.asList(usuario));

        List<UsuarioDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreCompleto()).isEqualTo("Juan Pérez");
        assertThat(resultado.get(0).getNombreRol()).isEqualTo("Administrador");
        assertThat(resultado.get(0).getUsername()).isEqualTo("juan@email.cl");
    }

    @Test
    void testObtenerUsuarioPorId() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");

        Usuario usuario = new Usuario();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setPersona(persona);
        usuario.setRol(rol);

        when(usuarioRepository.findByIdPersonaWithPersonaAndRol(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getNombreCompleto()).isEqualTo("Juan Pérez");
    }

    @Test
    void testCrearUsuario() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdPersona(1L);
        usuarioDTO.setIdRol(1L);

        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("María");
        persona.setApellido("González");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Cliente");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdPersona(1L);
        usuarioGuardado.setIdRol(1L);
        usuarioGuardado.setPersona(persona);
        usuarioGuardado.setRol(rol);

        when(personaRepository.existsById(1L)).thenReturn(true);
        when(rolRepository.existsById(1L)).thenReturn(true);
        when(usuarioRepository.existsByIdPersona(1L)).thenReturn(false);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioDTO resultado = usuarioService.crearUsuario(usuarioDTO);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getNombreCompleto()).isEqualTo("María González");
        assertThat(resultado.getNombreRol()).isEqualTo("Cliente");
    }

    @Test
    void testActualizarRolUsuario() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        Rol rolAntiguo = new Rol();
        rolAntiguo.setIdRol(1L);
        rolAntiguo.setNombreRol("Transportista");

        Rol rolNuevo = new Rol();
        rolNuevo.setIdRol(2L);
        rolNuevo.setNombreRol("Cliente");

        Usuario usuario = new Usuario();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setPersona(persona);
        usuario.setRol(rolAntiguo);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.existsById(2L)).thenReturn(true);
        when(rolRepository.findById(2L)).thenReturn(Optional.of(rolNuevo));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO resultado = usuarioService.actualizarRolUsuario(1L, 2L);

        assertThat(resultado.getIdRol()).isEqualTo(2L);
        assertThat(resultado.getNombreRol()).isEqualTo("Cliente");
    }

    @Test
    void testEliminarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        boolean resultado = usuarioService.eliminarUsuario(1L);

        assertThat(resultado).isTrue();
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testObtenerUsuariosPorRol() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");

        Usuario usuario = new Usuario();
        usuario.setIdPersona(1L);
        usuario.setIdRol(1L);
        usuario.setPersona(persona);
        usuario.setRol(rol);

        when(usuarioRepository.findByIdRol(1L)).thenReturn(Arrays.asList(usuario));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        List<UsuarioDTO> resultado = usuarioService.obtenerUsuariosPorRol(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreRol()).isEqualTo("Administrador");
    }
}
