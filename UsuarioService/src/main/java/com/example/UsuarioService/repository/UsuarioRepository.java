package com.example.UsuarioService.repository;

import com.example.UsuarioService.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuarios por rol
    List<Usuario> findByIdRol(Long idRol);
    
    // Buscar usuario por persona
    Optional<Usuario> findByIdPersona(Long idPersona);
    
    // Verificar si existe usuario para una persona
    boolean existsByIdPersona(Long idPersona);
    
    // Obtener usuarios con información de persona y rol
    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona JOIN FETCH u.rol")
    List<Usuario> findAllWithPersonaAndRol();
    
    // Obtener usuario con persona y rol por ID
    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona JOIN FETCH u.rol WHERE u.idPersona = :idPersona")
    Optional<Usuario> findByIdPersonaWithPersonaAndRol(Long idPersona);
}
