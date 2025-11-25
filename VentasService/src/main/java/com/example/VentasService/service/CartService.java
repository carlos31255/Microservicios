package com.example.VentasService.service;

import com.example.VentasService.model.CartItem;
import com.example.VentasService.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${services.inventario.url:http://localhost:8082}")
    private String inventarioServiceUrl;

    public CartService(CartItemRepository cartItemRepository, WebClient.Builder webClientBuilder) {
        this.cartItemRepository = cartItemRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public List<CartItem> getCartForCliente(Long clienteId) {
        return cartItemRepository.findByClienteId(clienteId);
    }

    public Long addOrUpdate(CartItem item, String talla) {
        // resolve talla label to id if needed
        Long resolvedTallaId = resolveTallaId(item.getTallaId(), item.getModeloId(), talla);
        item.setTallaId(resolvedTallaId);

        // if an item exists with same clienteId, modeloId and tallaId, update quantity
        return cartItemRepository.findByClienteIdAndModeloIdAndTallaId(item.getClienteId(), item.getModeloId(), item.getTallaId())
                .map(existing -> {
                    existing.setCantidad(item.getCantidad());
                    existing.setPrecioUnitario(item.getPrecioUnitario());
                    existing.setNombreProducto(item.getNombreProducto());
                    cartItemRepository.save(existing);
                    return existing.getId();
                })
                .orElseGet(() -> cartItemRepository.save(item).getId());
    }

    public int getCountByCliente(Long clienteId) {
        return cartItemRepository.countByClienteId(clienteId);
    }

    public void delete(CartItem item) {
        if (item.getId() != null) cartItemRepository.deleteById(item.getId());
    }

    public void clear(Long clienteId) {
        cartItemRepository.deleteByClienteId(clienteId);
    }

    public CartItem getItem(Long clienteId, Long modeloId, Long tallaId, String talla) {
        Long resolvedTallaId = resolveTallaId(tallaId, modeloId, talla);
        if (resolvedTallaId != null) {
            return cartItemRepository.findByClienteIdAndModeloIdAndTallaId(clienteId, modeloId, resolvedTallaId).orElse(null);
        }
        return cartItemRepository.findByClienteIdAndModeloId(clienteId, modeloId).orElse(null);
    }

    // Return human readable talla label for a tallaId; empty string when unknown
    public String getTallaLabel(Long tallaId) {
        if (tallaId == null) return "";
        try {
            WebClient client = webClientBuilder.baseUrl(inventarioServiceUrl).build();
            Map<String,Object>[] list = client.get()
                    .uri("/inventario/tallas")
                    .retrieve()
                    .bodyToMono(Map[].class)
                    .block(Duration.ofSeconds(5));

            if (list != null) {
                for (Map<String,Object> m : list) {
                    Object idObj = m.get("id");
                    Long id = null;
                    if (idObj instanceof Number) id = ((Number) idObj).longValue();
                    else {
                        try { id = Long.valueOf(idObj.toString()); } catch (Exception e) { }
                    }
                    if (id != null && id.equals(tallaId)) {
                        Object valor = m.get("valor");
                        return valor != null ? valor.toString() : "";
                    }
                }
            }
        } catch (Exception ex) {
            // ignore and return empty string
        }
        return "";
    }

    private Long resolveTallaId(Long tallaId, Long modeloId, String talla) {
        if (tallaId != null) return tallaId;
        if (talla == null) return null;

        try {
            WebClient client = webClientBuilder.baseUrl(inventarioServiceUrl).build();
            // Call /inventario/tallas and find matching valor
            Map<String,Object>[] list = client.get()
                    .uri("/inventario/tallas")
                    .retrieve()
                    .bodyToMono(Map[].class)
                    .block(Duration.ofSeconds(5));

            if (list != null) {
                for (Map<String,Object> m : list) {
                    Object valor = m.get("valor");
                    if (valor != null && talla.equalsIgnoreCase(valor.toString())) {
                        Object idObj = m.get("id");
                        if (idObj instanceof Number) return ((Number) idObj).longValue();
                        try { return Long.valueOf(idObj.toString()); } catch (Exception ex) { }
                    }
                }
            }
        } catch (Exception ex) {
            // ignore and return null if lookup fails
        }
        return null;
    }
}
