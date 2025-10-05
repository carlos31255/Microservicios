package com.example.geografiaservice.repository;

import com.example.geografiaservice.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    
    //Buscar ciudad por nombre ignorando mayúsculas y minúsculas
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
    
    //Buscar todas las ciudades de una región específica
    List<Ciudad> findByRegionId(Long regionId);
    
    //Buscar todas las ciudades de una región ordenadas por nombre
    List<Ciudad> findByRegionIdOrderByNombreAsc(Long regionId);

    //Verificar si existe una ciudad con un nombre específico en una región
    boolean existsByNombreIgnoreCaseAndRegionId(String nombre, Long regionId);

}
