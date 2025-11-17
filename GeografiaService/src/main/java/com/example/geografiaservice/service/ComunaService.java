package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.ComunaDTO;
import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.ComunaRepository;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComunaService {
    
    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    //Obtener todas las comunas
    public List<ComunaDTO> obtenerTodasLasComunas() {
        return comunaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Obtener comuna por ID
    public ComunaDTO obtenerComunaPorId(Long id) {
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id));
        return convertirADTO(comuna);
    }

    //Obtener comunas por región
    public List<ComunaDTO> obtenerComunasPorRegion(Long regionId) {
        return comunaRepository.findByRegionIdOrderByNombreAsc(regionId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Obtener comunas por ciudad
    public List<ComunaDTO> obtenerComunasPorCiudad(Long ciudadId) {
        return comunaRepository.findByCiudadIdOrderByNombreAsc(ciudadId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Obtener comunas por región y ciudad
    public List<ComunaDTO> obtenerComunasPorRegionYCiudad(Long regionId, Long ciudadId) {
        return comunaRepository.findByRegionIdAndCiudadId(regionId, ciudadId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Buscar comunas por nombre parcial
    public List<ComunaDTO> buscarComunasPorNombre(String nombre) {
        return comunaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //Crear nueva comuna
    public Comuna guardarComuna(Comuna comuna) {
        //Verificar que la región existe
        if (!regionRepository.existsById(comuna.getRegion().getId())) {
            throw new RuntimeException("Región no encontrada con id: " + comuna.getRegion().getId());
        }

        //Verificar que la ciudad existe
        if (!ciudadRepository.existsById(comuna.getCiudad().getId())) {
            throw new RuntimeException("Ciudad no encontrada con id: " + comuna.getCiudad().getId());
        }

        //Verificar que no existe otra comuna con el mismo nombre en la ciudad
        if (comunaRepository.existsByNombreIgnoreCaseAndCiudadId(comuna.getNombre(), comuna.getCiudad().getId())) {
            throw new RuntimeException("Ya existe una comuna con el nombre: " + comuna.getNombre() + " en esta ciudad");
        }

        return comunaRepository.save(comuna);
    }

    //Actualizar comuna existente
    public Comuna modificarComuna(Long id, Comuna comunaActualizada) {
        Comuna comunaExistente = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id));

        //Verificar que la región existe
        if (!regionRepository.existsById(comunaActualizada.getRegion().getId())) {
            throw new RuntimeException("Región no encontrada con id: " + comunaActualizada.getRegion().getId());
        }

        //Verificar que la ciudad existe
        if (!ciudadRepository.existsById(comunaActualizada.getCiudad().getId())) {
            throw new RuntimeException("Ciudad no encontrada con id: " + comunaActualizada.getCiudad().getId());
        }

        comunaExistente.setNombre(comunaActualizada.getNombre());
        comunaExistente.setRegion(comunaActualizada.getRegion());
        comunaExistente.setCiudad(comunaActualizada.getCiudad());

        return comunaRepository.save(comunaExistente);
    }

    //Eliminar comuna
    public void eliminarComuna(Long id) {
        if (!comunaRepository.existsById(id)) {
            throw new RuntimeException("Comuna no encontrada con id: " + id);
        }
        comunaRepository.deleteById(id);
    }

    //Convertir entidad a DTO
    private ComunaDTO convertirADTO(Comuna comuna) {
        return new ComunaDTO(
                comuna.getId(),
                comuna.getNombre(),
                comuna.getRegion().getId(),
                comuna.getCiudad().getId()
        );
    }
}
