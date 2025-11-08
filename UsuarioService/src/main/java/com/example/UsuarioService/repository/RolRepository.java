package com.example.UsuarioService.repository;

import com.example.UsuarioService.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    // Buscar por nombre de rol
    Optional<Rol> findByNombreRol(String nombreRol);
    
    // Verificar si existe por nombre
    boolean existsByNombreRol(String nombreRol);
}
