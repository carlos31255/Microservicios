package com.example.ventasservice.service;

import com.example.ventasservice.model.CartItem;
import com.example.ventasservice.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private CartItem item1;

    @BeforeEach
    void setUp() {
        item1 = new CartItem();
        item1.setId(1L);
        item1.setClienteId(100L);
        item1.setModeloId(10L);
        item1.setTallaId(42L);
        item1.setCantidad(2);
        item1.setPrecioUnitario(25000);
        item1.setNombreProducto("Zapatilla Nike");
    }

    @Test
    void testGetCartForCliente() {
        when(cartItemRepository.findByClienteId(100L)).thenReturn(Arrays.asList(item1));

        List<CartItem> lista = cartService.getCartForCliente(100L);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNombreProducto()).isEqualTo("Zapatilla Nike");
        verify(cartItemRepository).findByClienteId(100L);
    }

    @Test
    void testAddOrUpdate_newItem() {
        CartItem newItem = new CartItem();
        newItem.setClienteId(200L);
        newItem.setModeloId(20L);
        newItem.setTallaId(40L);
        newItem.setCantidad(1);
        newItem.setPrecioUnitario(30000);
        newItem.setNombreProducto("Zapatilla Adidas");

        when(cartItemRepository.findByClienteIdAndModeloIdAndTallaId(200L, 20L, 40L)).thenReturn(Optional.empty());
        CartItem saved = new CartItem(); saved.setId(5L);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(saved);

        Long id = cartService.addOrUpdate(newItem, null);

        assertThat(id).isEqualTo(5L);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void testAddOrUpdate_updateExisting() {
        CartItem existing = new CartItem();
        existing.setId(2L);
        existing.setClienteId(100L);
        existing.setModeloId(10L);
        existing.setTallaId(42L);
        existing.setCantidad(1);
        existing.setPrecioUnitario(25000);
        existing.setNombreProducto("Zapatilla Nike");

        when(cartItemRepository.findByClienteIdAndModeloIdAndTallaId(100L, 10L, 42L)).thenReturn(Optional.of(existing));
        when(cartItemRepository.save(eq(existing))).thenReturn(existing);

        CartItem update = new CartItem();
        update.setClienteId(100L);
        update.setModeloId(10L);
        update.setTallaId(42L);
        update.setCantidad(3);

        Long id = cartService.addOrUpdate(update, null);

        assertThat(id).isEqualTo(2L);
        verify(cartItemRepository).save(existing);
        assertThat(existing.getCantidad()).isEqualTo(3);
    }

    @Test
    void testGetCountByCliente() {
        when(cartItemRepository.countByClienteId(100L)).thenReturn(4);

        int count = cartService.getCountByCliente(100L);

        assertThat(count).isEqualTo(4);
        verify(cartItemRepository).countByClienteId(100L);
    }

    @Test
    void testDeleteAndClearAndGetItem() {
        // delete
        CartItem toDelete = new CartItem(); toDelete.setId(9L);
        cartService.delete(toDelete);
        verify(cartItemRepository).deleteById(9L);

        // clear
        cartService.clear(100L);
        verify(cartItemRepository).deleteByClienteId(100L);

        // getItem
        when(cartItemRepository.findByClienteIdAndModeloIdAndTallaId(100L, 10L, 42L)).thenReturn(Optional.of(item1));
        CartItem found = cartService.getItem(100L, 10L, 42L, null);
        assertThat(found).isNotNull();
        assertThat(found.getNombreProducto()).isEqualTo("Zapatilla Nike");
    }
}
