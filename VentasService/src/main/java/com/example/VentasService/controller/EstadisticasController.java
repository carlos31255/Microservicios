package com.example.VentasService.controller;

import com.example.VentasService.dto.DetalleVentaDTO;
import com.example.VentasService.dto.ReporteVentasDTO;
import com.example.VentasService.model.Boleta;
import com.example.VentasService.model.FiltroReporteRequest;
import com.example.VentasService.repository.BoletaRepository;
import com.example.VentasService.repository.DetalleBoletaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ventas/estadisticas")
@Tag(name = "Estadísticas", description = "Estadísticas y reportes de ventas")
public class EstadisticasController {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    @GetMapping
    @Operation(summary = "Dashboard completo de ventas", description = "Retorna estadísticas generales del sistema de ventas")
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

    @PostMapping("/generar")
    @Operation(summary = "Generar reporte de ventas", description = "Reporte con filtro de año/mes")
    public ResponseEntity<ReporteVentasDTO> generarReporte(@RequestBody FiltroReporteRequest filtro) {

        if (filtro.getAnio() == null) {
            return ResponseEntity.badRequest().build();
        }

        LocalDateTime inicio;
        LocalDateTime fin;

        // Lógica para definir el rango de fechas
        if (filtro.getMes() != null) {
            // CASO 1: Reporte Mensual (Ej: Noviembre 2024)
            // Calculamos el primer día del mes a las 00:00:00
            YearMonth yearMonth = YearMonth.of(filtro.getAnio(), filtro.getMes());
            inicio = yearMonth.atDay(1).atStartOfDay();

            // Calculamos el último día del mes a las 23:59:59.999
            fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        } else {
            // CASO 2: Reporte Anual (Todo el año 2024)
            // Desde 1 de Enero 00:00
            inicio = LocalDateTime.of(filtro.getAnio(), 1, 1, 0, 0);

            // Hasta 31 de Diciembre 23:59
            fin = LocalDateTime.of(filtro.getAnio(), 12, 31, 23, 59, 59);
        }

        // 1. Buscar usando BETWEEN (Sin @Query)
        List<Boleta> boletas = boletaRepository.findByFechaVentaBetween(inicio, fin);

        // 2. Filtrar realizadas (completada/confirmada)
        List<Boleta> ventasRealizadas = boletas.stream()
                .filter(b -> "completada".equalsIgnoreCase(b.getEstado())
                        || "confirmada".equalsIgnoreCase(b.getEstado()))
                .toList();

        // 3. Filtrar canceladas
        List<Boleta> ventasCanceladas = boletas.stream()
                .filter(b -> "cancelada".equalsIgnoreCase(b.getEstado()))
                .toList();

        // 4. Calcular ingresos (solo de las realizadas)
        int ingresosTotal = ventasRealizadas.stream()
                .mapToInt(b -> b.getTotal() != null ? b.getTotal() : 0)
                .sum();

        // 5. Mapear a DetalleVentaDTO
        List<DetalleVentaDTO> detalles = ventasRealizadas.stream()
                .map(b -> new DetalleVentaDTO(
                        // ID a String
                        String.valueOf(b.getId()),

                        // LocalDateTime (fechaVenta) a Long (Milisegundos)
                        b.getFechaVenta() != null
                                ? b.getFechaVenta().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                : 0L,

                        // Como solo tenemos ID, devolvemos el ID formateado
                        "Cliente #" + b.getClienteId(),

                        b.getTotal(),
                        b.getEstado()))
                .collect(Collectors.toList());

        // 6. Retornar respuesta
        ReporteVentasDTO reporte = new ReporteVentasDTO(
                ventasRealizadas.size(),
                ventasCanceladas.size(),
                ingresosTotal,
                detalles);

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Estadísticas por cliente", description = "Retorna estadísticas de compras de un cliente específico")
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
