package com.example.geografiaservice.controller;

import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.service.ComunaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geografia/comunas")
@CrossOrigin(origins = "*")
public class ComunaController {

    private final ComunaService comunaService;

    public ComunaController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    //Obtener todas las comunas
    @GetMapping
    public ResponseEntity<List<Comuna>> obtenerTodasLasComunas() {
        try {
            List<Comuna> comunas = comunaService.obtenerTodasLasComunas();
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comuna por ID
    @GetMapping("/{id}")
    public ResponseEntity<Comuna> obtenerComunaPorId(@PathVariable Long id) {
        try {
            Comuna comuna = comunaService.obtenerComunaPorId(id);
            return ResponseEntity.ok(comuna);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por región
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorRegion(@PathVariable Long regionId) {
        try {
            List<Comuna> comunas = comunaService.obtenerComunasPorRegion(regionId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por ciudad
    @GetMapping("/ciudad/{ciudadId}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorCiudad(@PathVariable Long ciudadId) {
        try {
            List<Comuna> comunas = comunaService.obtenerComunasPorCiudad(ciudadId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por región y ciudad
    @GetMapping("/region/{regionId}/ciudad/{ciudadId}")
    public ResponseEntity<List<Comuna>> obtenerComunasPorRegionYCiudad(
            @PathVariable Long regionId, 
            @PathVariable Long ciudadId) {
        try {
            List<Comuna> comunas = comunaService.obtenerComunasPorRegionYCiudad(regionId, ciudadId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Buscar comunas por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Comuna>> buscarComunasPorNombre(@RequestParam String nombre) {
        try {
            List<Comuna> comunas = comunaService.buscarComunasPorNombre(nombre);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Crear nueva comuna
    @PostMapping("/crear")
    public ResponseEntity<Comuna> guardarComuna(@RequestBody Comuna comuna) {
        try {
            Comuna nuevaComuna = comunaService.guardarComuna(comuna);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaComuna);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Actualizar comuna
    @PutMapping("/{id}")
    public ResponseEntity<Comuna> modificarComuna(@PathVariable Long id, @RequestBody Comuna comuna) {
        try {
            Comuna comunaActualizada = comunaService.modificarComuna(id, comuna);
            return ResponseEntity.ok(comunaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Eliminar comuna
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarComuna(@PathVariable Long id) {
        try {
            comunaService.eliminarComuna(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
