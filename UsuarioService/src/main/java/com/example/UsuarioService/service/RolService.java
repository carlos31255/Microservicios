package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.RolDTO;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // Obtener todos los roles
    public List<RolDTO> obtenerTodosLosRoles() {
        return rolRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener rol por ID
    public RolDTO obtenerRolPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        return convertirADTO(rol);
    }

    // Obtener rol por nombre
    public RolDTO obtenerRolPorNombre(String nombreRol) {
        Rol rol = rolRepository.findByNombreRol(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + nombreRol));
        return convertirADTO(rol);
    }

    // Crear rol
    public RolDTO crearRol(RolDTO rolDTO) {
        if (rolRepository.existsByNombreRol(rolDTO.getNombreRol())) {
            throw new IllegalArgumentException("Ya existe un rol con nombre: " + rolDTO.getNombreRol());
        }
        Rol rol = new Rol(null, rolDTO.getNombreRol(), rolDTO.getDescripcion());
        Rol rolGuardado = rolRepository.save(rol);
        return convertirADTO(rolGuardado);
    }

    // Actualizar rol
    public RolDTO actualizarRol(Long id, RolDTO rolDTO) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol == null) {
            return null;
        }

        rol.setNombreRol(rolDTO.getNombreRol());
        rol.setDescripcion(rolDTO.getDescripcion());

        Rol rolActualizado = rolRepository.save(rol);
        return convertirADTO(rolActualizado);
    }

    // Eliminar rol
    public boolean eliminarRol(Long id) {
        if (!rolRepository.existsById(id)) {
            return false;
        }
        rolRepository.deleteById(id);
        return true;
    }

    // Convertir Rol a RolDTO
    private RolDTO convertirADTO(Rol rol) {
        return new RolDTO(
                rol.getIdRol(),
                rol.getNombreRol(),
                rol.getDescripcion()
        );
    }
}
