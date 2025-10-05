package com.example.geografiaservice.repository;

import com.example.geografiaservice.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {
    
    //Buscar comuna por nombre ignorando mayúsculas y minúsculas
    Optional<Comuna> findByNombreIgnoreCase(String nombre);

    //Buscar todas las comunas de una región específica
    List<Comuna> findByRegionId(Long regionId);

    //Buscar todas las comunas de una región ordenadas por nombre
    List<Comuna> findByRegionIdOrderByNombreAsc(Long regionId);
    
    //Buscar todas las comunas de una ciudad específica
    List<Comuna> findByCiudadId(Long ciudadId);
    
    //Buscar todas las comunas de una ciudad ordenadas por nombre
    List<Comuna> findByCiudadIdOrderByNombreAsc(Long ciudadId);
    
    //Buscar comunas por región y ciudad
    List<Comuna> findByRegionIdAndCiudadId(Long regionId, Long ciudadId);
    
    //Verificar si existe una comuna con un nombre específico en una ciudad
    boolean existsByNombreIgnoreCaseAndCiudadId(String nombre, Long ciudadId);
    
    //Buscar comunas por nombre parcial (búsqueda flexible)
    List<Comuna> findByNombreContainingIgnoreCase(String nombre);
 
}
