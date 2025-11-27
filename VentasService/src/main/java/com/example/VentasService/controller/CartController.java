package com.example.VentasService.controller;

import com.example.VentasService.dto.CartItemRequest;
import com.example.VentasService.dto.CartItemResponse;
import com.example.VentasService.model.CartItem;
import com.example.VentasService.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
@Tag(name = "Carrito de Compras", description = "Gestión temporal de productos seleccionados por el cliente")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Obtener carrito del cliente", description = "Recupera todos los ítems guardados en el carrito de compras de un cliente específico")
    @GetMapping("/{clienteId}")
    public List<CartItemResponse> getCart(@PathVariable Long clienteId) {
        return cartService.getCartForCliente(clienteId).stream().map(this::toDto).toList();
    }

    @Operation(summary = "Agregar o Actualizar ítem", description = "Añade un producto al carrito o actualiza la cantidad si ya existe. El ID del cliente puede venir en el body o como parámetro.")
    @PostMapping()
    public ResponseEntity<List<CartItemResponse>> addOrUpdate(@RequestBody CartItemRequest req,
                                                              @Parameter(description = "ID del cliente (opcional si ya está en el body)") @RequestParam(required = false) Long clienteId) {
        CartItem item = new CartItem();
        item.setId(req.getId());
        if (req.getClienteId() != null) {
            item.setClienteId(req.getClienteId());
        } else if (clienteId != null) {
            item.setClienteId(clienteId);
        } else {
            item.setClienteId(null);
        }
        item.setModeloId(req.getModeloId());
        item.setTallaId(req.getTallaId());
        item.setCantidad(req.getCantidad());
        item.setPrecioUnitario(req.getPrecioUnitario());
        item.setNombreProducto(req.getNombreProducto());
        cartService.addOrUpdate(item, req.getTalla());
        List<CartItemResponse> updated = cartService.getCartForCliente(item.getClienteId()).stream().map(this::toDto).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @Operation(summary = "Agregar o Actualizar ítem (Vía Path)", description = "Endpoint alternativo para agregar ítems especificando el ID del cliente en la URL")
    @PostMapping("/{clienteId}")
    public ResponseEntity<List<CartItemResponse>> addOrUpdateWithPath(@PathVariable Long clienteId,
                                                                      @RequestBody CartItemRequest req) {
        if (req.getClienteId() == null) {
            req.setClienteId(clienteId);
        }
        return addOrUpdate(req, clienteId);
    }

    @Operation(summary = "Contar ítems", description = "Devuelve el número total de productos distintos en el carrito")
    @GetMapping("/{clienteId}/count")
    public int count(@PathVariable Long clienteId) {
        return cartService.getCountByCliente(clienteId);
    }

    @Operation(summary = "Eliminar un ítem", description = "Elimina un producto específico del carrito del cliente")
    @DeleteMapping("/{clienteId}/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long clienteId, @PathVariable Long id) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setClienteId(clienteId);
        cartService.delete(item);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito de un cliente")
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> clear(@PathVariable Long clienteId) {
        cartService.clear(clienteId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar ítem específico", description = "Verifica si existe un producto específico (modelo/talla) en el carrito y lo devuelve")
    @GetMapping("/{clienteId}/item")
    public ResponseEntity<CartItemResponse> getItem(@PathVariable Long clienteId,
                                                    @Parameter(description = "ID del modelo del producto") @RequestParam Long modeloId,
                                                    @Parameter(description = "ID de la talla") @RequestParam(required = false) Long tallaId,
                                                    @Parameter(description = "Nombre de la talla") @RequestParam(required = false) String talla) {
        CartItem item = cartService.getItem(clienteId, modeloId, tallaId, talla);
        if (item == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDto(item));
    }

    private CartItemResponse toDto(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setId(item.getId());
        dto.setClienteId(item.getClienteId());
        dto.setModeloId(item.getModeloId());
        dto.setTallaId(item.getTallaId());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setNombreProducto(item.getNombreProducto());
        dto.setFechaCreacion(item.getFechaCreacion());
        dto.setFechaActualizacion(item.getFechaActualizacion());
        dto.setTalla(cartService.getTallaLabel(item.getTallaId()));
        return dto;
    }
}
