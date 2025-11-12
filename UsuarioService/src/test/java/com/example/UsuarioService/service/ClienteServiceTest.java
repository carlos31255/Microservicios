package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.ClienteDTO;
import com.example.UsuarioService.model.Cliente;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.repository.ClienteRepository;
import com.example.UsuarioService.repository.PersonaRepository;
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
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testObtenerTodosLosClientes() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");
        persona.setEmail("juan@email.cl");
        persona.setTelefono("+56911111111");

        Cliente cliente = new Cliente();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");
        cliente.setPersona(persona);

        when(clienteRepository.findAllWithPersona()).thenReturn(Arrays.asList(cliente));

        List<ClienteDTO> resultado = clienteService.obtenerTodosLosClientes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCategoria()).isEqualTo("VIP");
        assertThat(resultado.get(0).getNombreCompleto()).isEqualTo("Juan Pérez");
        assertThat(resultado.get(0).getEmail()).isEqualTo("juan@email.cl");
    }

    @Test
    void testObtenerClientePorId() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setRut("12345678-9");

        Cliente cliente = new Cliente();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");
        cliente.setPersona(persona);

        when(clienteRepository.findByIdPersonaWithPersona(1L)).thenReturn(Optional.of(cliente));

        ClienteDTO resultado = clienteService.obtenerClientePorId(1L);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getCategoria()).isEqualTo("VIP");
        assertThat(resultado.getNombreCompleto()).isEqualTo("Juan Pérez");
    }

    @Test
    void testCrearCliente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setIdPersona(1L);
        clienteDTO.setCategoria("premium");

        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("María");
        persona.setApellido("González");
        persona.setRut("11111111-1");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setIdPersona(1L);
        clienteGuardado.setCategoria("premium");
        clienteGuardado.setPersona(persona);

        when(personaRepository.existsById(1L)).thenReturn(true);
        when(clienteRepository.existsById(1L)).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        ClienteDTO resultado = clienteService.crearCliente(clienteDTO);

        assertThat(resultado.getIdPersona()).isEqualTo(1L);
        assertThat(resultado.getCategoria()).isEqualTo("premium");
        assertThat(resultado.getNombreCompleto()).isEqualTo("María González");
    }

    @Test
    void testActualizarCategoria() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        Cliente cliente = new Cliente();
        cliente.setIdPersona(1L);
        cliente.setCategoria("regular");
        cliente.setPersona(persona);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        ClienteDTO resultado = clienteService.actualizarCategoria(1L, "VIP");

        assertThat(resultado.getCategoria()).isEqualTo("VIP");
    }

    @Test
    void testEliminarCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        boolean resultado = clienteService.eliminarCliente(1L);

        assertThat(resultado).isTrue();
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void testObtenerClientesPorCategoria() {
        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        Cliente cliente = new Cliente();
        cliente.setIdPersona(1L);
        cliente.setCategoria("VIP");
        cliente.setPersona(persona);

        when(clienteRepository.findByCategoria("VIP")).thenReturn(Arrays.asList(cliente));
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        List<ClienteDTO> resultado = clienteService.obtenerClientesPorCategoria("VIP");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCategoria()).isEqualTo("VIP");
    }
}
