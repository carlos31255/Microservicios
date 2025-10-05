package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CiudadService {
    
    private final CiudadRepository ciudadRepository;
    private final RegionRepository regionRepository;

    public CiudadService(CiudadRepository ciudadRepository, RegionRepository regionRepository) {
        this.ciudadRepository = ciudadRepository;
        this.regionRepository = regionRepository;
    }

    //Obtener todas las ciudades
    public List<Ciudad> obtenerTodasLasCiudades() {
        return ciudadRepository.findAll();
    }

    //Obtener ciudad por ID
    public Ciudad obtenerCiudadPorId(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada con id: " + id));
    }

    //Obtener ciudades por región
    public List<Ciudad> obtenerCiudadesPorRegion(Long regionId) {
        return ciudadRepository.findByRegionIdOrderByNombreAsc(regionId);
    }

    //Crear nueva ciudad
    public Ciudad guardarCiudad(Ciudad ciudad) {
        //Verificar que la región existe
        if (!regionRepository.existsById(ciudad.getRegion().getId())) {
            throw new RuntimeException("Región no encontrada con id: " + ciudad.getRegion().getId());
        }

        //Verificar que no existe otra ciudad con el mismo nombre en la región
        if (ciudadRepository.existsByNombreIgnoreCaseAndRegionId(ciudad.getNombre(), ciudad.getRegion().getId())) {
            throw new RuntimeException("Ya existe una ciudad con el nombre: " + ciudad.getNombre() + " en esta región");
        }

        return ciudadRepository.save(ciudad);
    }

    //Actualizar ciudad existente
    public Ciudad modificarCiudad(Long id, Ciudad ciudadActualizada) {
        Ciudad ciudadExistente = ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada con id: " + id));

        //Verificar que la región existe
        if (!regionRepository.existsById(ciudadActualizada.getRegion().getId())) {
            throw new RuntimeException("Región no encontrada con id: " + ciudadActualizada.getRegion().getId());
        }

        ciudadExistente.setNombre(ciudadActualizada.getNombre());
        ciudadExistente.setEsCapital(ciudadActualizada.getEsCapital());
        ciudadExistente.setRegion(ciudadActualizada.getRegion());

        return ciudadRepository.save(ciudadExistente);
    }

    //Eliminar ciudad
    public void eliminarCiudad(Long id) {
        if (!ciudadRepository.existsById(id)) {
            throw new RuntimeException("Ciudad no encontrada con id: " + id);
        }
        ciudadRepository.deleteById(id);
    }
}
