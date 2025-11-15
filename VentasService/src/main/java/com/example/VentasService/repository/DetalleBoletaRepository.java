package com.example.ventasservice.repository;

import com.example.ventasservice.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Integer> {
    
    // Obtener detalles de una boleta específica
    List<DetalleBoleta> findByBoletaId(Integer boletaId);
    
    // Obtener detalles ordenados por ID
    List<DetalleBoleta> findByBoletaIdOrderByIdAsc(Integer boletaId);
    
    // Buscar detalles por producto (inventario)
    List<DetalleBoleta> findByInventarioId(Integer inventarioId);
    
    // Contar productos en una boleta
    long countByBoletaId(Integer boletaId);
    
    // Eliminar todos los detalles de una boleta
    void deleteByBoletaId(Integer boletaId);
    
    // Obtener total de cantidad vendida de un producto
    @Query("SELECT SUM(d.cantidad) FROM DetalleBoleta d WHERE d.inventarioId = :inventarioId")
    Integer getTotalCantidadVendidaPorProducto(Integer inventarioId);
}
