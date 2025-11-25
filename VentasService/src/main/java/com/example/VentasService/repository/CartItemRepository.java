package com.example.VentasService.repository;

import com.example.VentasService.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByClienteId(Long clienteId);

    Optional<CartItem> findByClienteIdAndModeloIdAndTallaId(Long clienteId, Long modeloId, Long tallaId);

    Optional<CartItem> findByClienteIdAndModeloId(Long clienteId, Long modeloId);

    int countByClienteId(Long clienteId);

    void deleteByClienteId(Long clienteId);
}
