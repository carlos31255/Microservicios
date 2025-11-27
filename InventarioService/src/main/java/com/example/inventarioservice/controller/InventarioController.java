package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Inventario", description = "Gestión de existencias, control de stock y auditoría")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todo el inventario
    @Operation(summary = "Listar todo el inventario", description = "Devuelve una lista completa de todos los registros de inventario disponibles")
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodoElInventario() {
        try {
            List<InventarioDTO> inventario = inventarioService.obtenerTodoElInventario();
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener inventario por ID
    @Operation(summary = "Obtener inventario por ID", description = "Busca un registro de inventario específico mediante su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> obtenerInventarioPorId(@PathVariable Long id) {
        try {
            InventarioDTO inventario = inventarioService.obtenerInventarioPorId(id);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener inventario por productoId (todas las tallas)
    @Operation(summary = "Inventario por Producto", description = "Obtiene todas las variantes de stock (todas las tallas) asociadas a un ID de producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioDTO>> obtenerInventarioPorProductoId(@PathVariable Long productoId) {
        try {
            List<InventarioDTO> inventario = inventarioService.obtenerInventarioPorProductoId(productoId);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener inventario específico por productoId y talla
    @Operation(summary = "Inventario por Producto y Talla", description = "Busca el stock específico para una combinación de producto y talla")
    @GetMapping("/producto/{productoId}/talla/{talla}")
    public ResponseEntity<InventarioDTO> obtenerInventarioPorProductoIdYTalla(
            @PathVariable Long productoId,
            @PathVariable String talla) {
        try {
            InventarioDTO inventario = inventarioService.obtenerInventarioPorProductoIdYTalla(productoId, talla);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener productos con stock bajo
    @Operation(summary = "Alertas de Stock Bajo", description = "Lista los productos que se encuentran por debajo del nivel mínimo de stock definido")
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<InventarioDTO>> obtenerProductosConStockBajo() {
        try {
            List<InventarioDTO> inventario = inventarioService.obtenerProductosConStockBajo();
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar productos por nombre
    @Operation(summary = "Buscar productos", description = "Búsqueda de inventario por coincidencia de nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<InventarioDTO>> buscarProductosPorNombre(
            @Parameter(description = "Nombre o parte del nombre del producto a buscar") @RequestParam String nombre) {
        try {
            List<InventarioDTO> inventario = inventarioService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Crear nuevo inventario (usa DTO)
    @Operation(summary = "Registrar nuevo inventario", description = "Crea un nuevo registro de stock inicial para un producto")
    @PostMapping("/crear")
    public ResponseEntity<Inventario> crearInventario(@RequestBody InventarioDTO inventarioDTO) {
        try {
            Inventario nuevoInventario = inventarioService.crearInventario(inventarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Actualizar inventario (usa DTO)
    @Operation(summary = "Actualizar datos de inventario", description = "Modifica los datos básicos de un registro de inventario existente")
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(
            @PathVariable Long id,
            @RequestBody InventarioDTO inventarioDTO) {
        try {
            Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventarioDTO);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ajustar stock (entrada o salida)
    @Operation(summary = "Ajustar Stock (Entrada/Salida)", description = "Realiza un movimiento de inventario. El body debe contener: cantidad, tipo (ENTRADA/SALIDA), motivo y usuarioId")
    @PutMapping("/{id}/ajustar")
    public ResponseEntity<Inventario> ajustarStock(
            @PathVariable Long id,
            @RequestBody Map<String, Object> ajuste) {
        try {
            Integer cantidad = (Integer) ajuste.get("cantidad");
            String tipo = (String) ajuste.get("tipo");
            String motivo = (String) ajuste.get("motivo");
            Long usuarioId = ajuste.get("usuarioId") != null ?
                    Long.valueOf(ajuste.get("usuarioId").toString()) : null;

            Inventario inventarioActualizado = inventarioService.ajustarStock(
                    id, cantidad, tipo, motivo, usuarioId);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Verificar disponibilidad de stock
    @Operation(summary = "Verificar disponibilidad", description = "Comprueba si existe stock suficiente para una cantidad solicitada de un producto y talla")
    @GetMapping("/verificar-disponibilidad")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidad(
            @Parameter(description = "ID del producto") @RequestParam Long productoId,
            @Parameter(description = "Talla específica") @RequestParam String talla,
            @Parameter(description = "Cantidad requerida") @RequestParam Integer cantidad) {
        try {
            boolean disponible = inventarioService.verificarDisponibilidad(productoId, talla, cantidad);
            return ResponseEntity.ok(Map.of("disponible", disponible));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar inventario
    @Operation(summary = "Eliminar inventario", description = "Elimina permanentemente un registro de inventario del sistema")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        try {
            inventarioService.eliminarInventario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
