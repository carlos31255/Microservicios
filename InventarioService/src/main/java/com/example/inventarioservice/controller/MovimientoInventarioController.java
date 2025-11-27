package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.service.MovimientoInventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Movimientos de Inventario", description = "Consultas e historial de entradas y salidas de stock")
@RequestMapping("/inventario/movimientos")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // Obtener todos los movimientos
    @Operation(summary = "Listar todos los movimientos", description = "Devuelve el historial completo de movimientos registrados")
    @GetMapping
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerTodosLosMovimientos() {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerTodosLosMovimientos();
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener movimiento por ID
    @Operation(summary = "Obtener movimiento por ID", description = "Busca el detalle de un movimiento específico mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventarioDTO> obtenerMovimientoPorId(
            @Parameter(description = "ID único del movimiento", example = "101") 
            @PathVariable Long id) {
        try {
            MovimientoInventarioDTO movimiento = movimientoInventarioService.obtenerMovimientoPorId(id);
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener movimientos por inventarioId
    @Operation(summary = "Movimientos por Producto/Inventario", description = "Lista el historial de movimientos asociado a un ítem de inventario específico")
    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorInventarioId(
            @Parameter(description = "ID del registro de inventario", example = "50") 
            @PathVariable Long inventarioId) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService
                    .obtenerMovimientosPorInventarioId(inventarioId);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener movimientos por tipo (entrada/salida)
    @Operation(summary = "Filtrar por tipo", description = "Devuelve movimientos filtrados por tipo (ej. ENTRADA, SALIDA, AJUSTE)")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorTipo(
            @Parameter(description = "Tipo de movimiento", example = "ENTRADA") 
            @PathVariable String tipo) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorTipo(tipo);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener movimientos por usuarioId
    @Operation(summary = "Movimientos por Usuario", description = "Lista los movimientos realizados por un usuario específico")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorUsuarioId(
            @Parameter(description = "ID del usuario que realizó la acción", example = "12") 
            @PathVariable Long usuarioId) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService
                    .obtenerMovimientosPorUsuarioId(usuarioId);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
