package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.Inventario;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    // Buscar por productoId
    Optional<Inventario> findByProductoId(Long productoId);
    
    // Buscar por productoId y talla
    Optional<Inventario> findByProductoIdAndTalla(Long productoId, String talla);
    
    // Buscar todos los inventarios de un producto (todas las tallas)
    List<Inventario> findAllByProductoId(Long productoId);
    
    // Buscar productos con stock bajo (cantidad <= stock mínimo)
    List<Inventario> findByCantidadLessThanEqual(Integer stockMinimo);
    
    // Verificar si existe inventario para un producto y talla
    boolean existsByProductoIdAndTalla(Long productoId, String talla);
    
    // Buscar por nombre de producto (parcial, ignorando mayúsculas)
    List<Inventario> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por modelo y talla (usado en la precarga para evitar duplicados)
    Optional<Inventario> findByModeloIdAndTalla(Long modeloId, String talla);
}
