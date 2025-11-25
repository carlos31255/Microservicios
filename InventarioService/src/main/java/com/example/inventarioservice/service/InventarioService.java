package com.example.inventarioservice.service;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.model.MovimientosInventario;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioService {
    
    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    // Obtener todo el inventario
    public List<InventarioDTO> obtenerTodoElInventario() {
        return inventarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inventario por ID
    public InventarioDTO obtenerInventarioPorId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        return convertirADTO(inventario);
    }

    // Obtener inventario por productoId
    public List<InventarioDTO> obtenerInventarioPorProductoId(Long productoId) {
        return inventarioRepository.findAllByProductoId(productoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inventario específico por productoId y talla
    public InventarioDTO obtenerInventarioPorProductoIdYTalla(Long productoId, String talla) {
        Inventario inventario = inventarioRepository.findByProductoIdAndTalla(productoId, talla)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para producto: " 
                    + productoId + " talla: " + talla));
        return convertirADTO(inventario);
    }

    // Obtener productos con stock bajo
    public List<InventarioDTO> obtenerProductosConStockBajo() {
        return inventarioRepository.findAll().stream()
                .filter(inv -> inv.getCantidad() <= inv.getStockMinimo())
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar productos por nombre
    public List<InventarioDTO> buscarProductosPorNombre(String nombre) {
        return inventarioRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Crear nuevo inventario
    public Inventario crearInventario(Inventario inventario) {
        // Verificar que no existe inventario para el mismo producto y talla
        if (inventarioRepository.existsByProductoIdAndTalla(
                inventario.getProductoId(), inventario.getTalla())) {
            throw new RuntimeException("Ya existe inventario para el producto: " 
                + inventario.getProductoId() + " talla: " + inventario.getTalla());
        }

        // Validar cantidad inicial
        if (inventario.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Guardar inventario
        Inventario nuevoInventario = inventarioRepository.save(inventario);

        // Registrar movimiento de entrada inicial
        if (inventario.getCantidad() > 0) {
            registrarMovimiento(nuevoInventario.getId(), "entrada", 
                inventario.getCantidad(), "Stock inicial", null);
        }

        return nuevoInventario;
    }

    // Actualizar inventario
    public Inventario actualizarInventario(Long id, Inventario inventarioActualizado) {
        Inventario inventarioExistente = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));

        inventarioExistente.setNombre(inventarioActualizado.getNombre());
        inventarioExistente.setStockMinimo(inventarioActualizado.getStockMinimo());
        
        return inventarioRepository.save(inventarioExistente);
    }

    // Ajustar stock (entrada o salida)
    public Inventario ajustarStock(Long id, Integer cantidad, String tipo, String motivo, Long usuarioId) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));

        // Validar tipo de movimiento
        if (!tipo.equals("entrada") && !tipo.equals("salida")) {
            throw new RuntimeException("Tipo de movimiento inválido. Use 'entrada' o 'salida'");
        }

        // Validar cantidad
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        // Calcular nueva cantidad
        int nuevaCantidad = inventario.getCantidad();
        if (tipo.equals("entrada")) {
            nuevaCantidad += cantidad;
        } else {
            nuevaCantidad -= cantidad;
            if (nuevaCantidad < 0) {
                throw new RuntimeException("Stock insuficiente. Stock actual: " + inventario.getCantidad());
            }
        }

        // Actualizar cantidad
        inventario.setCantidad(nuevaCantidad);
        Inventario inventarioActualizado = inventarioRepository.save(inventario);

        // Registrar movimiento
        registrarMovimiento(id, tipo, cantidad, motivo, usuarioId);

        return inventarioActualizado;
    }

    // Verificar disponibilidad de stock
    public boolean verificarDisponibilidad(Long productoId, String talla, Integer cantidadRequerida) {
        return inventarioRepository.findByProductoIdAndTalla(productoId, talla)
                .map(inv -> inv.getCantidad() >= cantidadRequerida)
                .orElse(false);
    }

    // Eliminar inventario
    public void eliminarInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Inventario no encontrado con id: " + id);
        }
        inventarioRepository.deleteById(id);
    }

    // Método privado para registrar movimientos
    private void registrarMovimiento(Long inventarioId, String tipo, Integer cantidad, 
                                     String motivo, Long usuarioId) {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setInventarioId(inventarioId);
        movimiento.setTipo(tipo);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);
        movimiento.setUsuarioId(usuarioId);
        
        movimientoInventarioRepository.save(movimiento);
    }

    // Convertir entidad a DTO
    private InventarioDTO convertirADTO(Inventario inventario) {
        return new InventarioDTO(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getNombre(),
                inventario.getTalla(),
                inventario.getCantidad(),
                inventario.getStockMinimo()
        );
    }
}
