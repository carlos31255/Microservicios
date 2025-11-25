package com.example.inventarioservice.controller;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Obtener todo el inventario
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
    @GetMapping("/buscar")
    public ResponseEntity<List<InventarioDTO>> buscarProductosPorNombre(@RequestParam String nombre) {
        try {
            List<InventarioDTO> inventario = inventarioService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Crear nuevo inventario
    @PostMapping("/crear")
    public ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        try {
            Inventario nuevoInventario = inventarioService.crearInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Actualizar inventario
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(
            @PathVariable Long id, 
            @RequestBody Inventario inventario) {
        try {
            Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventario);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ajustar stock (entrada o salida)
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
    @GetMapping("/verificar-disponibilidad")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidad(
            @RequestParam Long productoId,
            @RequestParam String talla,
            @RequestParam Integer cantidad) {
        try {
            boolean disponible = inventarioService.verificarDisponibilidad(productoId, talla, cantidad);
            return ResponseEntity.ok(Map.of("disponible", disponible));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar inventario
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
