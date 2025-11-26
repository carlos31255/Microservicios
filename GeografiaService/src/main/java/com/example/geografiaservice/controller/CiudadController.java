package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.service.CiudadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geografia/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    //Obtener todas las ciudades
    @GetMapping
    public ResponseEntity<List<CiudadDTO>> obtenerTodasLasCiudades() {
        try {
            List<CiudadDTO> ciudades = ciudadService.obtenerTodasLasCiudades();
            return ResponseEntity.ok(ciudades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener ciudad por ID
    @GetMapping("/{id}")
    public ResponseEntity<CiudadDTO> obtenerCiudadPorId(@PathVariable Long id) {
        try {
            CiudadDTO ciudad = ciudadService.obtenerCiudadPorId(id);
            return ResponseEntity.ok(ciudad);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener ciudades por región
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<CiudadDTO>> obtenerCiudadesPorRegion(@PathVariable Long regionId) {
        try {
            List<CiudadDTO> ciudades = ciudadService.obtenerCiudadesPorRegion(regionId);
            return ResponseEntity.ok(ciudades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Crear nueva ciudad
    @PostMapping("/crear")
    public ResponseEntity<Ciudad> guardarCiudad(@RequestBody Ciudad ciudad) {
        try {
            Ciudad nuevaCiudad = ciudadService.guardarCiudad(ciudad);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCiudad);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Actualizar ciudad
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> modificarCiudad(@PathVariable Long id, @RequestBody Ciudad ciudad) {
        try {
            Ciudad ciudadActualizada = ciudadService.modificarCiudad(id, ciudad);
            return ResponseEntity.ok(ciudadActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Eliminar ciudad
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Long id) {
        try {
            ciudadService.eliminarCiudad(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
