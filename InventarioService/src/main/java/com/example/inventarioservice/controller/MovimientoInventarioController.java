package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.service.MovimientoInventarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoInventarioController {
    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // Obtener todos los movimientos
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
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventarioDTO> obtenerMovimientoPorId(@PathVariable Long id) {
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
    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorInventarioId(
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
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorTipo(@PathVariable String tipo) {
        try {
            List<MovimientoInventarioDTO> movimientos = movimientoInventarioService.obtenerMovimientosPorTipo(tipo);
            return ResponseEntity.ok(movimientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener movimientos por usuarioId
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoInventarioDTO>> obtenerMovimientosPorUsuarioId(
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
