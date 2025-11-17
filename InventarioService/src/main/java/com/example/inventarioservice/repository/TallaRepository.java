package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.Talla;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {
	Talla findByValor(String valor);
}
