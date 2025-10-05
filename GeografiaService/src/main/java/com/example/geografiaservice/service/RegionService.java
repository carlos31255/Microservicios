package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegionService {
    
    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    //Obtener todas las regiones ordenadas
    public List<Region> obtenerTodasLasRegiones() {
        return regionRepository.findAllByOrderByOrdenAsc();
    }

    //Obtener región por ID
    public Region obtenerRegionPorId(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región no encontrada con id: " + id));
    }

    //Obtener región por código
    public Region obtenerRegionPorCodigo(String codigo) {
        return regionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Región no encontrada con código: " + codigo));
    }

    //Crear nueva región
    public Region guardarRegion(Region region) {
        if (regionRepository.existsByCodigo(region.getCodigo())) {
            throw new RuntimeException("Ya existe una región con el código: " + region.getCodigo());
        }
        return regionRepository.save(region);
    }

    //Actualizar región existente
    public Region modificarRegion(Long id, Region regionActualizada) {
        Region regionExistente = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región no encontrada con id: " + id));

        regionExistente.setCodigo(regionActualizada.getCodigo());
        regionExistente.setNombre(regionActualizada.getNombre());
        regionExistente.setOrden(regionActualizada.getOrden());

        return regionRepository.save(regionExistente);
    }

    //Eliminar región
    public void eliminarRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new RuntimeException("Región no encontrada con id: " + id);
        }
        regionRepository.deleteById(id);
    }
}
