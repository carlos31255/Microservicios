package com.example.VentasService.repository;

import com.example.VentasService.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    
    // Buscar boletas por cliente
    List<Boleta> findByClienteId(Long clienteId);
    
    // Buscar boletas por estado
    List<Boleta> findByEstado(String estado);
    
    // Buscar boletas por cliente y estado
    List<Boleta> findByClienteIdAndEstado(Long clienteId, String estado);
    
    // Buscar boletas por rango de fechas
    //List<Boleta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar boletas por cliente y rango de fechas
    List<Boleta> findByClienteIdAndFechaVentaBetween(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Ordenar boletas por fecha descendente
    List<Boleta> findAllByOrderByFechaVentaDesc();
    
    // Buscar boletas por cliente ordenadas por fecha descendente
    List<Boleta> findByClienteIdOrderByFechaVentaDesc(Long clienteId);

    // Buscar boletas por rango de fechas
    @Query("SELECT b FROM Boleta b WHERE b.fechaVenta >= :inicio AND b.fechaVenta <= :fin ORDER BY b.fechaVenta DESC")
    List<Boleta> findByFechaVentaBetween(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin
    );
    // Contar boletas por estado
    long countByEstado(String estado);
    
    // Contar boletas por cliente
    long countByClienteId(Long clienteId);
}
