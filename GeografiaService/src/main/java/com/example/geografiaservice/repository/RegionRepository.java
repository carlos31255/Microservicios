package com.example.geografiaservice.repository;

import com.example.geografiaservice.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    
    //Buscar región por código (ej: "RM", "V", "VIII")
    Optional<Region> findByCodigo(String codigo);
    
    //Buscar región por nombre ignorando mayúsculas y minúsculas
    Optional<Region> findByNombreIgnoreCase(String nombre);
    
    //Buscar regiones ordenadas por el campo 'orden'
    List<Region> findAllByOrderByOrdenAsc();
    
    //Verificar si existe una región con un código específico
    boolean existsByCodigo(String codigo);
}
