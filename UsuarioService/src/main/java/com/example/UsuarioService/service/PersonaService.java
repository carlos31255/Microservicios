package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.repository.PersonaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Obtener todas las personas
    public List<PersonaDTO> obtenerTodasLasPersonas() {
        return personaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener persona por ID
    public PersonaDTO obtenerPersonaPorId(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id));
        return convertirADTO(persona);
    }

    // Obtener persona por RUT
    public PersonaDTO obtenerPersonaPorRut(String rut) {
        Persona persona = personaRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con RUT: " + rut));
        return convertirADTO(persona);
    }

    // Obtener persona por username
    public PersonaDTO obtenerPersonaPorUsername(String username) {
        Persona persona = personaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con username: " + username));
        return convertirADTO(persona);
    }

    // Buscar personas por nombre o apellido
    public List<PersonaDTO> buscarPersonasPorNombre(String nombre) {
        return personaRepository.findByNombreContainingOrApellidoContaining(nombre, nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener personas por estado
    public List<PersonaDTO> obtenerPersonasPorEstado(String estado) {
        return personaRepository.findByEstado(estado)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Crear persona
    public PersonaDTO crearPersona(PersonaDTO personaDTO) {
        // Verificar que no exista el RUT
        if (personaRepository.existsByRut(personaDTO.getRut())) {
            throw new IllegalArgumentException("Ya existe una persona con RUT: " + personaDTO.getRut());
        }
        // Verificar que no exista el username
        if (personaRepository.existsByUsername(personaDTO.getUsername())) {
            throw new IllegalArgumentException("Ya existe una persona con username: " + personaDTO.getUsername());
        }
        
        Persona persona = new Persona();
        persona.setNombre(personaDTO.getNombre());
        persona.setApellido(personaDTO.getApellido());
        persona.setRut(personaDTO.getRut());
        persona.setTelefono(personaDTO.getTelefono());
        persona.setEmail(personaDTO.getEmail());
        persona.setIdComuna(personaDTO.getIdComuna());
        persona.setCalle(personaDTO.getCalle());
        persona.setNumeroPuerta(personaDTO.getNumeroPuerta());
        persona.setUsername(personaDTO.getUsername());
        // Hashear la contraseña con BCrypt
        if (personaDTO.getPassword() != null && !personaDTO.getPassword().isEmpty()) {
            persona.setPassHash(passwordEncoder.encode(personaDTO.getPassword()));
        } else {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        persona.setFechaRegistro(System.currentTimeMillis());
        persona.setEstado("activo");
        
        Persona personaGuardada = personaRepository.save(persona);
        return convertirADTO(personaGuardada);
    }

    // Actualizar persona
    public PersonaDTO actualizarPersona(Long id, PersonaDTO personaDTO) {
        Persona persona = personaRepository.findById(id).orElse(null);
        if (persona == null) {
            return null;
        }

        persona.setNombre(personaDTO.getNombre());
        persona.setApellido(personaDTO.getApellido());
        persona.setTelefono(personaDTO.getTelefono());
        persona.setEmail(personaDTO.getEmail());
        persona.setIdComuna(personaDTO.getIdComuna());
        persona.setCalle(personaDTO.getCalle());
        persona.setNumeroPuerta(personaDTO.getNumeroPuerta());
        persona.setEstado(personaDTO.getEstado());

        Persona personaActualizada = personaRepository.save(persona);
        return convertirADTO(personaActualizada);
    }

    // Eliminar persona (borrado lógico)
    public boolean eliminarPersona(Long id) {
        Persona persona = personaRepository.findById(id).orElse(null);
        if (persona == null) {
            return false;
        }
        persona.setEstado("inactivo");
        personaRepository.save(persona);
        return true;
    }

    // Verificar credenciales (para login)
    public PersonaDTO verificarCredenciales(String username, String password) {
        Persona persona = personaRepository.findByUsername(username).orElse(null);
        if (persona != null && passwordEncoder.matches(password, persona.getPassHash())) {
            return convertirADTO(persona);
        }
        return null;
    }

    // Cambiar contraseña
    public boolean cambiarContrasena(Long id, String passwordActual, String passwordNueva) {
        Persona persona = personaRepository.findById(id).orElse(null);
        if (persona == null) {
            return false;
        }
        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordActual, persona.getPassHash())) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }
        // Establecer nueva contraseña hasheada
        persona.setPassHash(passwordEncoder.encode(passwordNueva));
        personaRepository.save(persona);
        return true;
    }

    // Establecer contraseña (para administradores o reseteo)
    public boolean establecerContrasena(Long id, String passwordNueva) {
        Persona persona = personaRepository.findById(id).orElse(null);
        if (persona == null) {
            return false;
        }
        persona.setPassHash(passwordEncoder.encode(passwordNueva));
        personaRepository.save(persona);
        return true;
    }

    // Convertir Persona a PersonaDTO
    private PersonaDTO convertirADTO(Persona persona) {
        return new PersonaDTO(
                persona.getIdPersona(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getRut(),
                persona.getTelefono(),
                persona.getEmail(),
                persona.getIdComuna(),
                persona.getCalle(),
                persona.getNumeroPuerta(),
                persona.getUsername(),
                null, // password - nunca se devuelve en consultas
                persona.getFechaRegistro(),
                persona.getEstado()
        );
    }
}
