package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CiudadService {
    
    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private RegionRepository regionRepository;

    //Obtener todas las ciudades
    public List<CiudadDTO> obtenerTodasLasCiudades() {
        return ciudadRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Obtener ciudad por ID
    public CiudadDTO obtenerCiudadPorId(Long id) {
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada con id: " + id));
        return convertirADTO(ciudad);
    }

    //Obtener ciudades por región
    public List<CiudadDTO> obtenerCiudadesPorRegion(Long regionId) {
        return ciudadRepository.findByRegionIdOrderByNombreAsc(regionId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
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

    //Convertir entidad a DTO
    private CiudadDTO convertirADTO(Ciudad ciudad) {
        return new CiudadDTO(
                ciudad.getId(),
                ciudad.getNombre(),
                ciudad.getRegion().getId()
        );
    }
}
