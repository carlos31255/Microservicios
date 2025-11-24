package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Producto findByNombreIgnoreCase(String nombre);
}
