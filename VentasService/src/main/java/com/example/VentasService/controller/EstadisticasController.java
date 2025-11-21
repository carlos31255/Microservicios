package com.example.ventasservice.controller;

import com.example.ventasservice.repository.BoletaRepository;
import com.example.ventasservice.repository.DetalleBoletaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas/estadisticas")
@Tag(name = "Estadísticas", description = "Estadísticas y reportes de ventas")
public class EstadisticasController {
    
    @Autowired
    private BoletaRepository boletaRepository;
    
    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;
    
    @GetMapping
    @Operation(summary = "Dashboard completo de ventas",
               description = "Retorna estadísticas generales del sistema de ventas")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Map<String, Object> stats = new HashMap<>();
        
        // Totales generales
        stats.put("totalBoletas", boletaRepository.count());
        stats.put("totalProductosVendidos", detalleBoletaRepository.count());
        
        // Por estado
        stats.put("boletasPendientes", boletaRepository.countByEstado("pendiente"));
        stats.put("boletasConfirmadas", boletaRepository.countByEstado("confirmada"));
        stats.put("boletasCompletadas", boletaRepository.countByEstado("completada"));
        stats.put("boletasCanceladas", boletaRepository.countByEstado("cancelada"));
        
        // Total de ventas
        Integer totalVentas = boletaRepository.findAll().stream()
                .mapToInt(b -> b.getTotal() != null ? b.getTotal() : 0)
                .sum();
        stats.put("totalVentas", totalVentas);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/cliente/{id}")
    @Operation(summary = "Estadísticas por cliente",
               description = "Retorna estadísticas de compras de un cliente específico")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasCliente(@PathVariable Long id) {
        Map<String, Object> stats = new HashMap<>();
        
        long totalCompras = boletaRepository.countByClienteId(id);
        stats.put("totalCompras", totalCompras);
        
        // Total gastado por el cliente
        Integer totalGastado = boletaRepository.findByClienteId(id).stream()
                .mapToInt(b -> b.getTotal() != null ? b.getTotal() : 0)
                .sum();
        stats.put("totalGastado", totalGastado);
        
        // Promedio de compra
        stats.put("promedioCompra", totalCompras > 0 ? totalGastado / totalCompras : 0);
        
        return ResponseEntity.ok(stats);
    }
}
