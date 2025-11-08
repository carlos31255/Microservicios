package com.example.UsuarioService.repository;

import com.example.UsuarioService.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar clientes por categoría
    List<Cliente> findByCategoria(String categoria);
    
    // Obtener clientes con información de persona
    @Query("SELECT c FROM Cliente c JOIN FETCH c.persona")
    List<Cliente> findAllWithPersona();
    
    // Obtener cliente con persona por ID
    @Query("SELECT c FROM Cliente c JOIN FETCH c.persona WHERE c.idPersona = :idPersona")
    Optional<Cliente> findByIdPersonaWithPersona(Long idPersona);
}
