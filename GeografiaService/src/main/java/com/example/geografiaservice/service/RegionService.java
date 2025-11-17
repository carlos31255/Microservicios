package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegionService {
    
    @Autowired
    private RegionRepository regionRepository;

    //Obtener todas las regiones ordenadas
    public List<RegionDTO> obtenerTodasLasRegiones() {
        return regionRepository.findAllByOrderByOrdenAsc()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Obtener región por ID
    public RegionDTO obtenerRegionPorId(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Región no encontrada con id: " + id));
        return convertirADTO(region);
    }

    //Obtener región por código
    public RegionDTO obtenerRegionPorCodigo(String codigo) {
        Region region = regionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Región no encontrada con código: " + codigo));
        return convertirADTO(region);
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

    //Convertir entidad a DTO
    private RegionDTO convertirADTO(Region region) {
        return new RegionDTO(
                region.getId(),
                region.getNombre(),
                region.getCodigo(),
                region.getOrden()
        );
    }
}
