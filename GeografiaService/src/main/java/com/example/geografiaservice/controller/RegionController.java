package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geografia/regiones")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    //Obtener todas las regiones
    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerTodasLasRegiones() {
        try {
            List<RegionDTO> regiones = regionService.obtenerTodasLasRegiones();
            return ResponseEntity.ok(regiones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener región por ID
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> obtenerRegionPorId(@PathVariable Long id) {
        try {
            RegionDTO region = regionService.obtenerRegionPorId(id);
            return ResponseEntity.ok(region);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener región por código
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<RegionDTO> obtenerRegionPorCodigo(@PathVariable String codigo) {
        try {
            RegionDTO region = regionService.obtenerRegionPorCodigo(codigo);
            return ResponseEntity.ok(region);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Crear nueva región
    @PostMapping("/crear")
    public ResponseEntity<Region> guardarRegion(@RequestBody Region region) {
        try {
            Region nuevaRegion = regionService.guardarRegion(region);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRegion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Actualizar región
    @PutMapping("/{id}")
    public ResponseEntity<Region> modificarRegion(@PathVariable Long id, @RequestBody Region region) {
        try {
            Region regionActualizada = regionService.modificarRegion(id, region);
            return ResponseEntity.ok(regionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Eliminar región
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarRegion(@PathVariable Long id) {
        try {
            regionService.eliminarRegion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
