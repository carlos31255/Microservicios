package com.example.inventarioservice.service;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.model.MovimientosInventario;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TallaRepository tallaRepository;

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

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


    // Crear nuevo inventario a partir de DTO (busca producto y talla por id)
    public Inventario crearInventario(InventarioDTO dto) {
        // Buscar producto
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getProductoId()));

        // Buscar talla
        Talla talla = tallaRepository.findById(dto.getTallaId())
                .orElseThrow(() -> new RuntimeException("Talla no encontrada con id: " + dto.getTallaId()));

        // Verificar que no existe inventario para el mismo producto y talla
        if (inventarioRepository.existsByProductoIdAndTalla(producto.getId(), talla.getValor())) {
            throw new RuntimeException("Ya existe inventario para el producto: "
                    + producto.getId() + " talla: " + talla.getValor());
        }

        // Validar cantidad inicial
        if (dto.getCantidad() != null && dto.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        // Crear entidad Inventario
        Inventario nuevo = new Inventario();
        nuevo.setProductoId(producto.getId());
        nuevo.setNombre(producto.getNombre());
        nuevo.setTalla(talla.getValor());
        nuevo.setCantidad(dto.getCantidad() != null ? dto.getCantidad() : 0);
        nuevo.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0);

        Inventario inventarioGuardado = inventarioRepository.save(nuevo);

        // Asegurar asociación producto <-> talla
        asociarTallaAProducto(producto, talla);

        // Registrar movimiento de entrada inicial (si aplica)
        if (inventarioGuardado.getCantidad() > 0) {
            registrarMovimiento(inventarioGuardado.getId(), "entrada",
                    inventarioGuardado.getCantidad(), "Stock inicial", null);
        }

        return inventarioGuardado;
    }

    // Actualizar inventario
    public Inventario actualizarInventario(Long id, InventarioDTO dto) {
        log.debug(">>> actualizarInventario id={} - productoId={}, tallaId={}, talla={}, cantidad={}", 
                 id, dto.getProductoId(), dto.getTallaId(), dto.getTalla(), dto.getCantidad());
        
        Inventario existente = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));

        // Si cambió el producto o la talla, verificar unicidad
        boolean cambioProducto = dto.getProductoId() != null && !dto.getProductoId().equals(existente.getProductoId());
        boolean cambioTalla = dto.getTalla() != null && !dto.getTalla().equals(existente.getTalla());

        if (cambioProducto || cambioTalla) {
            Long nuevoProductoId = dto.getProductoId() != null ? dto.getProductoId() : existente.getProductoId();
            String nuevaTalla = dto.getTalla() != null ? dto.getTalla() : existente.getTalla();

            if (inventarioRepository.existsByProductoIdAndTallaAndIdNot(nuevoProductoId, nuevaTalla, id)) {
                throw new RuntimeException(
                        "Ya existe inventario para el producto: " + nuevoProductoId + " talla: " + nuevaTalla);
            }
        }

        // Actualizar campos
        if (dto.getCantidad() != null) {
            int diferencia = dto.getCantidad() - existente.getCantidad();
            existente.setCantidad(dto.getCantidad());

            // Registrar movimiento si cambió la cantidad
            if (diferencia != 0) {
                String tipoMovimiento = diferencia > 0 ? "entrada" : "salida";
                registrarMovimiento(id, tipoMovimiento, Math.abs(diferencia), "Ajuste manual", null);
            }
        }

        if (dto.getStockMinimo() != null) {
            existente.setStockMinimo(dto.getStockMinimo());
        }

        if (dto.getNombre() != null) {
            existente.setNombre(dto.getNombre());
        }

        // Si cambió la talla, actualizar la asociación
        if (cambioTalla && dto.getTallaId() != null) {
            Talla nuevaTalla = tallaRepository.findById(dto.getTallaId())
                    .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
            existente.setTalla(nuevaTalla.getValor());

            // Asociar nueva talla al producto
            productoRepository.findById(existente.getProductoId()).ifPresent(producto -> {
                log.debug("Talla cambió, asociando nueva talla {} al producto {}", 
                         nuevaTalla.getValor(), producto.getId());
                asociarTallaAProducto(producto, nuevaTalla);
            });
        }

        // Incluso si no cambió la talla, aseguramos que esté en producto_talla
        if (dto.getTallaId() != null) {
            Talla tallaActual = tallaRepository.findById(dto.getTallaId())
                    .orElse(null);
            
            if (tallaActual != null) {
                productoRepository.findById(existente.getProductoId()).ifPresent(producto -> {
                    log.debug("Verificando asociación talla {} con producto {}", 
                             tallaActual.getValor(), producto.getId());
                    
                    // Verificar si la talla ya está asociada
                    boolean yaAsociada = producto.getTallas() != null && 
                                        producto.getTallas().stream()
                                        .anyMatch(t -> t.getId().equals(tallaActual.getId()));
                    
                    if (!yaAsociada) {
                        log.warn("⚠️ Talla {} NO estaba asociada al producto {}. Asociando ahora...", 
                                tallaActual.getValor(), producto.getId());
                        asociarTallaAProducto(producto, tallaActual);
                    } else {
                        log.debug("✅ Talla {} ya está correctamente asociada al producto {}", 
                                 tallaActual.getValor(), producto.getId());
                    }
                });
            }
        }

        Inventario inventarioActualizado = inventarioRepository.save(existente);
        log.debug("<<< Inventario actualizado exitosamente");
        
        return inventarioActualizado;
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

    /**
     * Asociar una talla a un producto (actualiza tabla producto_talla)
     */
    private void asociarTallaAProducto(Producto producto, Talla talla) {
        try {
            if (producto.getTallas() == null) {
                producto.setTallas(new java.util.HashSet<>());
            }

            // Verificar si ya existe la asociación
            boolean yaExiste = producto.getTallas().stream()
                    .anyMatch(t -> t.getId().equals(talla.getId()));

            if (!yaExiste) {
                producto.getTallas().add(talla);
                productoRepository.save(producto);
                log.debug("Talla {} asociada al producto {}. Total tallas: {}",
                        talla.getValor(), producto.getId(), producto.getTallas().size());
            } else {
                log.debug("La talla {} ya está asociada al producto {},",
                        talla.getValor(), producto.getId());
            }
        } catch (Exception e) {
            log.warn("No se pudo actualizar las tallas del producto {}: {}",
                    producto.getId(), e.getMessage());
        }
    }


    // Convertir entidad a DTO
    private InventarioDTO convertirADTO(Inventario inventario) {
    if (inventario == null) return null;
    
    // Obtener el ID de la talla buscando por valor
    Long tallaId = null;
    if (inventario.getTalla() != null) {
        Talla talla = tallaRepository.findByValor(inventario.getTalla());
        if (talla != null) {
            tallaId = talla.getId();
        }
    }
    
    return new InventarioDTO(
        inventario.getId(),              // id
        inventario.getProductoId(),      // productoId
        inventario.getNombre(),          // nombre
        tallaId,                         // tallaId
        inventario.getTalla(),           // talla
        inventario.getCantidad(),        // cantidad
        inventario.getStockMinimo()      // stockMinimo
    );
}

    // Método auxiliar para obtener el ID de la talla por su valor
    private Long obtenerTallaIdPorValor(String valorTalla) {
        if (valorTalla == null)
            return null;
        Talla talla = tallaRepository.findByValor(valorTalla);
        return talla != null ? talla.getId() : null;
    }
}
