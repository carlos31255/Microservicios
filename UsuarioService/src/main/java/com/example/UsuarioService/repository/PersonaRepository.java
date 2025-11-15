package com.example.UsuarioService.repository;

import com.example.UsuarioService.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    // Buscar por RUT
    Optional<Persona> findByRut(String rut);
    
    // Buscar por username
    Optional<Persona> findByUsername(String username);
    
    // Buscar por email
    Optional<Persona> findByEmail(String email);
    
    // Verificar si existe por RUT
    boolean existsByRut(String rut);
    
    // Verificar si existe por username
    boolean existsByUsername(String username);
    
    // Buscar por estado
    List<Persona> findByEstado(String estado);
    
    // Contar por estado
    long countByEstadoTrue();
    long countByEstadoFalse();
    
    // Buscar por nombre o apellido (parcial, ignorando mayúsculas)
    List<Persona> findByNombreContainingOrApellidoContaining(String nombre, String apellido);
}
