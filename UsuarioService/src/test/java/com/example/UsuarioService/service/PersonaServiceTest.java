package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.repository.PersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonaService personaService;

    @Test
    void testObtenerTodasLasPersonas() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");
        persona.setEmail("juan@email.cl");
        persona.setUsername("juan@email.cl");
        persona.setEstado("activo");

        when(personaRepository.findAll()).thenReturn(Arrays.asList(persona));

        List<PersonaDTO> resultado = personaService.obtenerTodasLasPersonas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        assertThat(resultado.get(0).getApellido()).isEqualTo("Pérez");
        assertThat(resultado.get(0).getRut()).isEqualTo("12345678-9");
    }

    @Test
    void testObtenerPersonaPorId() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        PersonaDTO resultado = personaService.obtenerPersonaPorId(1L);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Juan");
    }

    @Test
    void testObtenerPersonaPorRut() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setRut("12345678-9");

        when(personaRepository.findByRut("12345678-9")).thenReturn(Optional.of(persona));

        PersonaDTO resultado = personaService.obtenerPersonaPorRut("12345678-9");

        assertThat(resultado.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void testObtenerPersonaPorUsername() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setUsername("juan@email.cl");

        when(personaRepository.findByUsername("juan@email.cl")).thenReturn(Optional.of(persona));

        PersonaDTO resultado = personaService.obtenerPersonaPorUsername("juan@email.cl");

        assertThat(resultado.getUsername()).isEqualTo("juan@email.cl");
    }

    @Test
    void testCrearPersona() {
        PersonaDTO personaDTO = new PersonaDTO();
        personaDTO.setNombre("María");
        personaDTO.setApellido("González");
        personaDTO.setRut("11111111-1");
        personaDTO.setEmail("maria@email.cl");

        Persona personaGuardada = new Persona();
        personaGuardada.setIdPersona(1L);
        personaGuardada.setNombre("María");
        personaGuardada.setApellido("González");
        personaGuardada.setRut("11111111-1");
        personaGuardada.setEmail("maria@email.cl");
        personaGuardada.setPassHash("");
        personaGuardada.setEstado("activo");

        when(personaRepository.save(any(Persona.class))).thenReturn(personaGuardada);

        PersonaDTO resultado = personaService.crearPersona(personaDTO);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("María");
        assertThat(resultado.getApellido()).isEqualTo("González");
    }

    @Test
    void testActualizarPersona() {
        Persona personaExistente = new Persona();
        personaExistente.setIdPersona(1L);
        personaExistente.setNombre("Juan");
        personaExistente.setApellido("Pérez");

        PersonaDTO personaDTO = new PersonaDTO();
        personaDTO.setNombre("Juan Modificado");
        personaDTO.setApellido("Pérez Modificado");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaExistente));
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PersonaDTO resultado = personaService.actualizarPersona(1L, personaDTO);

        assertThat(resultado.getNombre()).isEqualTo("Juan Modificado");
        assertThat(resultado.getApellido()).isEqualTo("Pérez Modificado");
    }

    @Test
    void testEliminarPersona() {
        when(personaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = personaService.eliminarPersona(1L);

        assertThat(resultado).isTrue();
        verify(personaRepository).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");

        when(personaRepository.findByNombreContainingOrApellidoContaining("Juan", "Juan")).thenReturn(Arrays.asList(persona));

        List<PersonaDTO> resultado = personaService.buscarPersonasPorNombre("Juan");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
    }

    @Test
    void testFiltrarPorEstado() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setEstado("activo");

        when(personaRepository.findByEstado("activo")).thenReturn(Arrays.asList(persona));

        List<PersonaDTO> resultado = personaService.obtenerPersonasPorEstado("activo");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("activo");
    }

    @Test
    void testVerificarCredenciales() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");
        persona.setTelefono("+56911111111");
        persona.setEmail("juan@email.cl");
        persona.setIdComuna(197L);
        persona.setCalle("Av. Providencia");
        persona.setNumeroPuerta("123");
        persona.setUsername("juan@email.cl");
        persona.setPassHash("password123");
        persona.setFechaRegistro(System.currentTimeMillis());
        persona.setEstado("activo");

        when(personaRepository.findByUsername("juan@email.cl")).thenReturn(Optional.of(persona));

        PersonaDTO resultado = personaService.verificarCredenciales("juan@email.cl", "password123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsername()).isEqualTo("juan@email.cl");
    }
}
