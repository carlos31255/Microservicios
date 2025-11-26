package com.example.VentasService.service;

import com.example.VentasService.model.CartItem;
import com.example.VentasService.repository.CartItemRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private CartService cartService;

    @Test
    void getCountByCliente_returnsCount() {
        when(cartItemRepository.countByClienteId(10L)).thenReturn(3);

        int count = cartService.getCountByCliente(10L);

        assertThat(count).isEqualTo(3);
    }

    @Test
    void addOrUpdate_existingItem_updatesAndReturnsId() {
        CartItem existing = new CartItem(5L, 1L, 100L, 10L, 2, 500, "Zapato A", LocalDateTime.now(), LocalDateTime.now());
        when(cartItemRepository.findByClienteIdAndModeloIdAndTallaId(1L, 100L, 10L)).thenReturn(Optional.of(existing));
        when(cartItemRepository.save(existing)).thenReturn(existing);

        CartItem toUpdate = new CartItem(null, 1L, 100L, 10L, 4, 450, "Zapato A", null, null);
        Long resultId = cartService.addOrUpdate(toUpdate, null);

        assertThat(resultId).isEqualTo(5L);
        verify(cartItemRepository).save(existing);
    }

    @Test
    void addOrUpdate_newItem_savesAndReturnsId() {
        CartItem toSave = new CartItem(null, 2L, 200L, 20L, 1, 1000, "Camisa B", null, null);
        CartItem saved = new CartItem(8L, 2L, 200L, 20L, 1, 1000, "Camisa B", LocalDateTime.now(), LocalDateTime.now());

        when(cartItemRepository.findByClienteIdAndModeloIdAndTallaId(2L, 200L, 20L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any())).thenReturn(saved);

        Long id = cartService.addOrUpdate(toSave, null);

        assertThat(id).isEqualTo(8L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void getItem_byClienteAndModelo_returnsItem() {
        CartItem item = new CartItem(7L, 3L, 300L, null, 1, 700, "Polera C", LocalDateTime.now(), LocalDateTime.now());
        when(cartItemRepository.findByClienteIdAndModeloId(3L, 300L)).thenReturn(Optional.of(item));

        CartItem result = cartService.getItem(3L, 300L, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(7L);
    }
}
