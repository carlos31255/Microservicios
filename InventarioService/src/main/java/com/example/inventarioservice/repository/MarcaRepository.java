package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
}
