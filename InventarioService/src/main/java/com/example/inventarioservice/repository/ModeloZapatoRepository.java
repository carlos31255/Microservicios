package com.example.inventarioservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventarioservice.model.ModeloZapato;

@Repository
public interface ModeloZapatoRepository extends JpaRepository<ModeloZapato, Long> {
	ModeloZapato findByNombreIgnoreCase(String nombre);
}
