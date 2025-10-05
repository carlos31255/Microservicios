package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.ComunaRepository;
import com.example.geografiaservice.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComunaService {
    
    private final ComunaRepository comunaRepository;
    private final RegionRepository regionRepository;
    private final CiudadRepository ciudadRepository;

    public ComunaService(ComunaRepository comunaRepository, RegionRepository regionRepository, 
                         CiudadRepository ciudadRepository) {
        this.comunaRepository = comunaRepository;
        this.regionRepository = regionRepository;
        this.ciudadRepository = ciudadRepository;
    }

    //Obtener todas las comunas
    public List<Comuna> obtenerTodasLasComunas() {
        return comunaRepository.findAll();
    }

    //Obtener comuna por ID
    public Comuna obtenerComunaPorId(Long id) {
        return comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada con id: " + id));
    }

    //Obtener comunas por región
    public List<Comuna> obtenerComunasPorRegion(Long regionId) {
        return comunaRepository.findByRegionIdOrderByNombreAsc(regionId);
    }

    //Obtener comunas por ciudad
    public List<Comuna> obtenerComunasPorCiudad(Long ciudadId) {
        return comunaRepository.findByCiudadIdOrderByNombreAsc(ciudadId);
    }

    //Obtener comunas por región y ciudad
    public List<Comuna> obtenerComunasPorRegionYCiudad(Long regionId, Long ciudadId) {
        return comunaRepository.findByRegionIdAndCiudadId(regionId, ciudadId);
    }

    //Buscar comunas por nombre parcial
    public List<Comuna> buscarComunasPorNombre(String nombre) {
        return comunaRepository.findByNombreContainingIgnoreCase(nombre);
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
}
