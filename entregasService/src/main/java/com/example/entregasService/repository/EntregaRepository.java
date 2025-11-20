package com.example.entregasService.repository;

import com.example.entregasService.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    // Buscar entregas por idTransportista
    List<Entrega> findByIdTransportista(Integer idTransportista);
    // Buscar entregas por estadoEntrega
    List<Entrega> findByEstadoEntrega(String estadoEntrega);
}
