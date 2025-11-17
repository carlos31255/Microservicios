package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.UsuarioDTO;
import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.model.Usuario;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.RolRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RolRepository rolRepository;

    // Obtener todos los usuarios
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAllWithPersonaAndRol()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID de persona
    public UsuarioDTO obtenerUsuarioPorId(Long idPersona) {
        Usuario usuario = usuarioRepository.findByIdPersonaWithPersonaAndRol(idPersona)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con idPersona: " + idPersona));
        return convertirADTO(usuario);
    }

    // Obtener usuarios por rol
    public List<UsuarioDTO> obtenerUsuariosPorRol(Long idRol) {
        return usuarioRepository.findByIdRol(idRol)
                .stream()
                .map(usuario -> {
                    // Cargar persona y rol manualmente si es necesario
                    Persona persona = personaRepository.findById(usuario.getIdPersona()).orElse(null);
                    Rol rol = rolRepository.findById(usuario.getIdRol()).orElse(null);
                    return convertirADTO(usuario, persona, rol);
                })
                .collect(Collectors.toList());
    }

    // Crear usuario
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Verificar que la persona exista
        if (!personaRepository.existsById(usuarioDTO.getIdPersona())) {
            throw new IllegalArgumentException("Persona no encontrada con id: " + usuarioDTO.getIdPersona());
        }
        // Verificar que el rol exista
        if (!rolRepository.existsById(usuarioDTO.getIdRol())) {
            throw new IllegalArgumentException("Rol no encontrado con id: " + usuarioDTO.getIdRol());
        }
        // Verificar que no exista ya un usuario para esta persona
        if (usuarioRepository.existsByIdPersona(usuarioDTO.getIdPersona())) {
            throw new IllegalArgumentException("Ya existe un usuario para la persona con id: " + usuarioDTO.getIdPersona());
        }
        
        Usuario usuario = new Usuario();
        usuario.setIdPersona(usuarioDTO.getIdPersona());
        usuario.setIdRol(usuarioDTO.getIdRol());
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        Persona persona = personaRepository.findById(usuarioGuardado.getIdPersona()).orElse(null);
        Rol rol = rolRepository.findById(usuarioGuardado.getIdRol()).orElse(null);
        
        return convertirADTO(usuarioGuardado, persona, rol);
    }

    // Actualizar rol de usuario
    public UsuarioDTO actualizarRolUsuario(Long idPersona, Long nuevoIdRol) {
        Usuario usuario = usuarioRepository.findById(idPersona).orElse(null);
        if (usuario == null) {
            return null;
        }

        // Verificar que el rol exista
        if (!rolRepository.existsById(nuevoIdRol)) {
            throw new IllegalArgumentException("Rol no encontrado con id: " + nuevoIdRol);
        }

        usuario.setIdRol(nuevoIdRol);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        Persona persona = personaRepository.findById(usuarioActualizado.getIdPersona()).orElse(null);
        Rol rol = rolRepository.findById(usuarioActualizado.getIdRol()).orElse(null);
        
        return convertirADTO(usuarioActualizado, persona, rol);
    }

    // Eliminar usuario (borrado lógico)
    public boolean eliminarUsuario(Long idPersona) {
        Usuario usuario = usuarioRepository.findById(idPersona).orElse(null);
        if (usuario == null) {
            return false;
        }
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        return true;
    }

    // Convertir Usuario a UsuarioDTO
    private UsuarioDTO convertirADTO(Usuario usuario) {
        Persona persona = usuario.getPersona();
        Rol rol = usuario.getRol();
        
        String nombreCompleto = persona != null ? 
            persona.getNombre() + " " + persona.getApellido() : "";
        String username = persona != null ? persona.getUsername() : "";
        String nombreRol = rol != null ? rol.getNombreRol() : "";

        return new UsuarioDTO(
                usuario.getIdPersona(),
                usuario.getIdRol(),
                usuario.getActivo(),
                nombreCompleto,
                username,
                nombreRol
        );
    }

    // Convertir Usuario a UsuarioDTO (con persona y rol cargados manualmente)
    private UsuarioDTO convertirADTO(Usuario usuario, Persona persona, Rol rol) {
        String nombreCompleto = persona != null ? 
            persona.getNombre() + " " + persona.getApellido() : "";
        String username = persona != null ? persona.getUsername() : "";
        String nombreRol = rol != null ? rol.getNombreRol() : "";

        return new UsuarioDTO(
                usuario.getIdPersona(),
                usuario.getIdRol(),
                usuario.getActivo(),
                nombreCompleto,
                username,
                nombreRol
        );
    }
}
