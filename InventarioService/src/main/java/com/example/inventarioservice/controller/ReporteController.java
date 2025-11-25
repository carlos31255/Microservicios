package com.example.inventarioservice.controller;

import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.dto.EstadisticasGeneralesDTO;
import com.example.inventarioservice.dto.MovimientosEstadisticasDTO;
import com.example.inventarioservice.dto.ProductoEstadisticasDTO;
import com.example.inventarioservice.dto.TallaDetalleDTO;
import com.example.inventarioservice.dto.ProductoResumenDTO;
import com.example.inventarioservice.dto.TopStockDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@Tag(name = "Reportes", description = "Endpoints para estadísticas y reportes del inventario")
public class ReporteController {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @GetMapping("/estadisticas-generales")
    @Operation(summary = "Obtener estadísticas generales del inventario",
               description = "Retorna contadores de productos, stock total y alertas")
    public ResponseEntity<EstadisticasGeneralesDTO> obtenerEstadisticasGenerales() {
        List<Inventario> todosLosInventarios = inventarioRepository.findAll();

        // Contadores generales
        Long totalProductos = (long) todosLosInventarios.size();
        Long totalMovimientos = movimientoRepository.count();

        // Stock total
        int stockTotal = todosLosInventarios.stream()
                .mapToInt(Inventario::getCantidad)
                .sum();

        // Productos con stock bajo (cantidad <= stockMinimo)
        long productosStockBajo = todosLosInventarios.stream()
                .filter(inv -> inv.getCantidad() <= inv.getStockMinimo())
                .count();

        // Productos sin stock
        long productosSinStock = todosLosInventarios.stream()
                .filter(inv -> inv.getCantidad() == 0)
                .count();

        // Valor promedio de stock por producto
        double promedioStock = todosLosInventarios.isEmpty() ? 0 :
                (double) stockTotal / todosLosInventarios.size();
        double promedioRedondeado = Math.round(promedioStock * 100.0) / 100.0;

        EstadisticasGeneralesDTO dto = new EstadisticasGeneralesDTO(
                totalProductos,
                totalMovimientos,
                stockTotal,
                productosStockBajo,
                productosSinStock,
                promedioRedondeado
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/stock-bajo")
    @Operation(summary = "Obtener productos con stock bajo",
               description = "Retorna lista de productos cuya cantidad es menor o igual al stock mínimo")
    public ResponseEntity<List<ProductoResumenDTO>> obtenerProductosStockBajo() {
        List<ProductoResumenDTO> productos = inventarioRepository.findAll().stream()
                .filter(inv -> inv.getCantidad() <= inv.getStockMinimo())
                .map(inv -> new ProductoResumenDTO(
                        inv.getId(),
                        inv.getProductoId(),
                        inv.getNombre(),
                        inv.getTalla(),
                        inv.getCantidad(),
                        inv.getStockMinimo(),
                        inv.getStockMinimo() - inv.getCantidad()
                ))
                .toList();

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/movimientos/estadisticas")
    @Operation(summary = "Estadísticas de movimientos de inventario",
               description = "Retorna información sobre entradas y salidas de inventario")
    public ResponseEntity<MovimientosEstadisticasDTO> obtenerEstadisticasMovimientos() {
        long totalMovimientos = movimientoRepository.count();
        long movimientosEntrada = movimientoRepository.findByTipo("entrada").size();
        long movimientosSalida = movimientoRepository.findByTipo("salida").size();

        int totalEntradas = movimientoRepository.findByTipo("entrada").stream()
                .mapToInt(mov -> mov.getCantidad())
                .sum();
        int totalSalidas = movimientoRepository.findByTipo("salida").stream()
                .mapToInt(mov -> mov.getCantidad())
                .sum();

        MovimientosEstadisticasDTO dto = new MovimientosEstadisticasDTO(
                totalMovimientos,
                movimientosEntrada,
                movimientosSalida,
                totalEntradas,
                totalSalidas,
                totalEntradas - totalSalidas
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/productos/{productoId}/estadisticas")
    @Operation(summary = "Estadísticas de un producto específico",
               description = "Retorna información detallada de inventario y movimientos de un producto")
    public ResponseEntity<ProductoEstadisticasDTO> obtenerEstadisticasProducto(@PathVariable Long productoId) {
        List<Inventario> inventarios = inventarioRepository.findAllByProductoId(productoId);

        if (inventarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String nombreProducto = inventarios.get(0).getNombre();
        int totalTallas = inventarios.size();

        int stockTotal = inventarios.stream()
                .mapToInt(Inventario::getCantidad)
                .sum();

        List<TallaDetalleDTO> detalleTallas = inventarios.stream()
                .map(inv -> new TallaDetalleDTO(
                        inv.getTalla(),
                        inv.getCantidad(),
                        inv.getStockMinimo(),
                        inv.getCantidad() <= inv.getStockMinimo()
                ))
                .toList();

        List<Long> inventarioIds = inventarios.stream()
                .map(Inventario::getId)
                .toList();

        long totalMovimientos = inventarioIds.stream()
                .mapToLong(id -> movimientoRepository.findByInventarioId(id).size())
                .sum();

        ProductoEstadisticasDTO dto = new ProductoEstadisticasDTO(
                productoId,
                nombreProducto,
                totalTallas,
                stockTotal,
                detalleTallas,
                totalMovimientos
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/productos/top-stock")
    @Operation(summary = "Top 10 productos con mayor stock",
               description = "Retorna los 10 productos con mayor cantidad en inventario")
    public ResponseEntity<List<TopStockDTO>> obtenerTopProductosStock(@RequestParam(name = "limit", required = false) Integer limit) {
        int topLimit = (limit == null || limit <= 0) ? 10 : limit;

        List<TopStockDTO> topProductos = inventarioRepository.findAll().stream()
                .sorted((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()))
                .limit(topLimit)
                .map(inv -> new TopStockDTO(
                        inv.getProductoId(),
                        inv.getNombre(),
                        inv.getTalla(),
                        inv.getCantidad()
                ))
                .toList();

        return ResponseEntity.ok(topProductos);
    }
}
