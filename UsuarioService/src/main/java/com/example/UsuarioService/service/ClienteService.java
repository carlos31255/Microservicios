package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.ClienteDTO;
import com.example.UsuarioService.model.Cliente;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.repository.ClienteRepository;
import com.example.UsuarioService.repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    // Obtener todos los clientes
    public List<ClienteDTO> obtenerTodosLosClientes() {
        return clienteRepository.findAllWithPersona()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener cliente por ID
    public ClienteDTO obtenerClientePorId(Long idPersona) {
        Cliente cliente = clienteRepository.findByIdPersonaWithPersona(idPersona)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con idPersona: " + idPersona));
        return convertirADTO(cliente);
    }

    // Obtener clientes por categoría
    public List<ClienteDTO> obtenerClientesPorCategoria(String categoria) {
        return clienteRepository.findByCategoria(categoria)
                .stream()
                .map(cliente -> {
                    Persona persona = personaRepository.findById(cliente.getIdPersona()).orElse(null);
                    return convertirADTO(cliente, persona);
                })
                .collect(Collectors.toList());
    }

    // Crear cliente
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        // Verificar que la persona exista
        if (!personaRepository.existsById(clienteDTO.getIdPersona())) {
            throw new IllegalArgumentException("Persona no encontrada con id: " + clienteDTO.getIdPersona());
        }
        // Verificar que no exista ya un cliente para esta persona
        if (clienteRepository.existsById(clienteDTO.getIdPersona())) {
            throw new IllegalArgumentException("Ya existe un cliente para la persona con id: " + clienteDTO.getIdPersona());
        }
        
        Cliente cliente = new Cliente();
        cliente.setIdPersona(clienteDTO.getIdPersona());
        cliente.setCategoria(clienteDTO.getCategoria());
        
        Cliente clienteGuardado = clienteRepository.save(cliente);
        Persona persona = personaRepository.findById(clienteGuardado.getIdPersona()).orElse(null);
        
        return convertirADTO(clienteGuardado, persona);
    }

    // Actualizar categoría de cliente
    public ClienteDTO actualizarCategoria(Long idPersona, String nuevaCategoria) {
        Cliente cliente = clienteRepository.findById(idPersona).orElse(null);
        if (cliente == null) {
            return null;
        }

        cliente.setCategoria(nuevaCategoria);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        Persona persona = personaRepository.findById(clienteActualizado.getIdPersona()).orElse(null);
        
        return convertirADTO(clienteActualizado, persona);
    }

    // Eliminar cliente (borrado lógico)
    public boolean eliminarCliente(Long idPersona) {
        if (!clienteRepository.existsById(idPersona)) {
            return false;
        }
        clienteRepository.deleteById(idPersona);
        return true;
    }

    // Convertir Cliente a ClienteDTO
    private ClienteDTO convertirADTO(Cliente cliente) {
        Persona persona = cliente.getPersona();
        
        String nombreCompleto = persona != null ? 
            persona.getNombre() + " " + persona.getApellido() : "";
        String email = persona != null ? persona.getEmail() : "";
        String telefono = persona != null ? persona.getTelefono() : "";

        return new ClienteDTO(
                cliente.getIdPersona(),
                cliente.getCategoria(),
                cliente.getActivo(),
                nombreCompleto,
                email,
                telefono
        );
    }

    // Convertir Cliente a ClienteDTO (con persona cargada manualmente)
    private ClienteDTO convertirADTO(Cliente cliente, Persona persona) {
        String nombreCompleto = persona != null ? 
            persona.getNombre() + " " + persona.getApellido() : "";
        String email = persona != null ? persona.getEmail() : "";
        String telefono = persona != null ? persona.getTelefono() : "";

        return new ClienteDTO(
                cliente.getIdPersona(),
                cliente.getCategoria(),
                cliente.getActivo(),
                nombreCompleto,
                email,
                telefono
        );
    }
}
