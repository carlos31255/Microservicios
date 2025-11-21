package com.example.ventasservice.repository;

import com.example.ventasservice.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
    
    // Obtener detalles de una boleta específica
    List<DetalleBoleta> findByBoletaId(Long boletaId);
    
    // Obtener detalles ordenados por ID
    List<DetalleBoleta> findByBoletaIdOrderByIdAsc(Long boletaId);
    
    // Buscar detalles por producto (inventario)
    List<DetalleBoleta> findByInventarioId(Long inventarioId);
    
    // Contar productos en una boleta
    long countByBoletaId(Long boletaId);
    
    // Eliminar todos los detalles de una boleta
    void deleteByBoletaId(Long boletaId);
    
    // Obtener total de cantidad vendida de un producto
    @Query("SELECT SUM(d.cantidad) FROM DetalleBoleta d WHERE d.inventarioId = :inventarioId")
    Integer getTotalCantidadVendidaPorProducto(Long inventarioId);
}
