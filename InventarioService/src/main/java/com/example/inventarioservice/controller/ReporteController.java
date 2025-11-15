package com.example.inventarioservice.controller;

import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import com.example.inventarioservice.model.Inventario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Endpoints para estadísticas y reportes del inventario")
public class ReporteController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @GetMapping("/estadisticas-generales")
    @Operation(summary = "Obtener estadísticas generales del inventario",
               description = "Retorna contadores de productos, stock total y alertas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<Inventario> todosLosInventarios = inventarioRepository.findAll();
        
        // Contadores generales
        estadisticas.put("totalProductos", todosLosInventarios.size());
        estadisticas.put("totalMovimientos", movimientoRepository.count());
        
        // Stock total
        int stockTotal = todosLosInventarios.stream()
            .mapToInt(Inventario::getCantidad)
            .sum();
        estadisticas.put("stockTotal", stockTotal);
        
        // Productos con stock bajo (cantidad <= stockMinimo)
        long productosStockBajo = todosLosInventarios.stream()
            .filter(inv -> inv.getCantidad() <= inv.getStockMinimo())
            .count();
        estadisticas.put("productosConStockBajo", productosStockBajo);
        
        // Productos sin stock
        long productosSinStock = todosLosInventarios.stream()
            .filter(inv -> inv.getCantidad() == 0)
            .count();
        estadisticas.put("productosSinStock", productosSinStock);
        
        // Valor promedio de stock por producto
        double promedioStock = todosLosInventarios.isEmpty() ? 0 : 
            (double) stockTotal / todosLosInventarios.size();
        estadisticas.put("promedioStockPorProducto", Math.round(promedioStock * 100.0) / 100.0);
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Obtener productos con stock bajo",
               description = "Retorna lista de productos cuya cantidad es menor o igual al stock mínimo")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosStockBajo() {
        List<Map<String, Object>> productos = inventarioRepository.findAll().stream()
            .filter(inv -> inv.getCantidad() <= inv.getStockMinimo())
            .map(inv -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("id", inv.getId());
                producto.put("productoId", inv.getProductoId());
                producto.put("nombre", inv.getNombre());
                producto.put("talla", inv.getTalla());
                producto.put("cantidadActual", inv.getCantidad());
                producto.put("stockMinimo", inv.getStockMinimo());
                producto.put("diferencia", inv.getStockMinimo() - inv.getCantidad());
                return producto;
            })
            .toList();
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/movimientos/estadisticas")
    @Operation(summary = "Estadísticas de movimientos de inventario",
               description = "Retorna información sobre entradas y salidas de inventario")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasMovimientos() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        long totalMovimientos = movimientoRepository.count();
        long movimientosEntrada = movimientoRepository.findByTipo("entrada").size();
        long movimientosSalida = movimientoRepository.findByTipo("salida").size();
        
        estadisticas.put("totalMovimientos", totalMovimientos);
        estadisticas.put("movimientosEntrada", movimientosEntrada);
        estadisticas.put("movimientosSalida", movimientosSalida);
        
        // Cantidad total de entradas vs salidas
        int totalEntradas = movimientoRepository.findByTipo("entrada").stream()
            .mapToInt(mov -> mov.getCantidad())
            .sum();
        int totalSalidas = movimientoRepository.findByTipo("salida").stream()
            .mapToInt(mov -> mov.getCantidad())
            .sum();
        
        estadisticas.put("cantidadTotalEntradas", totalEntradas);
        estadisticas.put("cantidadTotalSalidas", totalSalidas);
        estadisticas.put("saldoMovimientos", totalEntradas - totalSalidas);
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/productos/{productoId}/estadisticas")
    @Operation(summary = "Estadísticas de un producto específico",
               description = "Retorna información detallada de inventario y movimientos de un producto")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasProducto(@PathVariable Long productoId) {
        List<Inventario> inventarios = inventarioRepository.findAllByProductoId(productoId);
        
        if (inventarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Información del producto
        estadisticas.put("productoId", productoId);
        estadisticas.put("nombreProducto", inventarios.get(0).getNombre());
        
        // Total de tallas disponibles
        estadisticas.put("totalTallas", inventarios.size());
        
        // Stock total del producto (todas las tallas)
        int stockTotal = inventarios.stream()
            .mapToInt(Inventario::getCantidad)
            .sum();
        estadisticas.put("stockTotal", stockTotal);
        
        // Detalle por talla
        List<Map<String, Object>> detalleTallas = inventarios.stream()
            .map(inv -> {
                Map<String, Object> talla = new HashMap<>();
                talla.put("talla", inv.getTalla());
                talla.put("cantidad", inv.getCantidad());
                talla.put("stockMinimo", inv.getStockMinimo());
                talla.put("alertaStockBajo", inv.getCantidad() <= inv.getStockMinimo());
                return talla;
            })
            .toList();
        estadisticas.put("detallePorTalla", detalleTallas);
        
        // Movimientos del producto (sumando todas las tallas)
        List<Long> inventarioIds = inventarios.stream()
            .map(Inventario::getId)
            .toList();
        
        long totalMovimientos = inventarioIds.stream()
            .mapToLong(id -> movimientoRepository.findByInventarioId(id).size())
            .sum();
        
        estadisticas.put("totalMovimientos", totalMovimientos);
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/productos/top-stock")
    @Operation(summary = "Top 10 productos con mayor stock",
               description = "Retorna los 10 productos con mayor cantidad en inventario")
    public ResponseEntity<List<Map<String, Object>>> obtenerTopProductosStock() {
        List<Map<String, Object>> topProductos = inventarioRepository.findAll().stream()
            .sorted((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()))
            .limit(10)
            .map(inv -> {
                Map<String, Object> producto = new HashMap<>();
                producto.put("productoId", inv.getProductoId());
                producto.put("nombre", inv.getNombre());
                producto.put("talla", inv.getTalla());
                producto.put("cantidad", inv.getCantidad());
                return producto;
            })
            .toList();
        
        return ResponseEntity.ok(topProductos);
    }
}
