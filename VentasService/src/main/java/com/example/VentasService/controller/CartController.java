package com.example.VentasService.controller;

import com.example.VentasService.dto.CartItemRequest;
import com.example.VentasService.dto.CartItemResponse;
import com.example.VentasService.model.CartItem;
import com.example.VentasService.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{clienteId}")
    public List<CartItemResponse> getCart(@PathVariable Long clienteId) {
        return cartService.getCartForCliente(clienteId).stream().map(this::toDto).toList();
    }

    @PostMapping()
    public ResponseEntity<List<CartItemResponse>> addOrUpdate(@RequestBody CartItemRequest req,
                                            @RequestParam(required = false) Long clienteId) {
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

    @PostMapping("/{clienteId}")
    public ResponseEntity<List<CartItemResponse>> addOrUpdateWithPath(@PathVariable Long clienteId,
                                                                      @RequestBody CartItemRequest req) {
        if (req.getClienteId() == null) {
            req.setClienteId(clienteId);
        }
        return addOrUpdate(req, clienteId);
    }

    @GetMapping("/{clienteId}/count")
    public int count(@PathVariable Long clienteId) {
        return cartService.getCountByCliente(clienteId);
    }

    @DeleteMapping("/{clienteId}/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long clienteId, @PathVariable Long id) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setClienteId(clienteId);
        cartService.delete(item);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> clear(@PathVariable Long clienteId) {
        cartService.clear(clienteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clienteId}/item")
    public ResponseEntity<CartItemResponse> getItem(@PathVariable Long clienteId,
                                          @RequestParam Long modeloId,
                                          @RequestParam(required = false) Long tallaId,
                                          @RequestParam(required = false) String talla) {
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
