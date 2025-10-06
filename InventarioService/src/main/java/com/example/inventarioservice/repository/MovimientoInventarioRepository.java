package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.MovimientosInventario;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientosInventario, Long> {
    
    // Buscar movimientos por inventarioId
    List<MovimientosInventario> findByInventarioId(Long inventarioId);
    
    // Buscar movimientos por tipo (entrada/salida)
    List<MovimientosInventario> findByTipo(String tipo);
    
    // Buscar movimientos por inventarioId ordenados por fecha descendente
    List<MovimientosInventario> findByInventarioIdOrderByIdDesc(Long inventarioId);
    
    // Buscar movimientos por usuarioId
    List<MovimientosInventario> findByUsuarioId(Long usuarioId);
}
