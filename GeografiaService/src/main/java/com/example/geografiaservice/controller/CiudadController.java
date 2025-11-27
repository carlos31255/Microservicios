package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.service.CiudadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geografia/ciudades")
@Tag(name = "Ciudades", description = "API para gestión geográfica de ciudades y regiones")
public class CiudadController {

    
    @Autowired
    private CiudadService ciudadService;

    //Obtener todas las ciudades
    @GetMapping
    @Operation(summary = "Listar todas las ciudades", description = "Devuelve el listado completo de ciudades registradas en el sistema")
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
    @Operation(summary = "Obtener ciudad por ID", description = "Busca los detalles de una ciudad específica mediante su identificador único")
    public ResponseEntity<CiudadDTO> obtenerCiudadPorId(
            @Parameter(description = "ID único de la ciudad", example = "1") 
            @PathVariable Long id) {
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
    @Operation(summary = "Listar ciudades por Región", description = "Obtiene todas las ciudades pertenecientes a una región específica")
    public ResponseEntity<List<CiudadDTO>> obtenerCiudadesPorRegion(
            @Parameter(description = "ID de la región", example = "5") 
            @PathVariable Long regionId) {
        try {
            List<CiudadDTO> ciudades = ciudadService.obtenerCiudadesPorRegion(regionId);
            return ResponseEntity.ok(ciudades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Crear nueva ciudad
    @PostMapping("/crear")
    @Operation(
        summary = "Crear nueva ciudad", 
        description = "Registra una nueva ciudad en la base de datos",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva ciudad",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Concepción\", \"regionId\": 8}"
                )
            )
        )
    )
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
    @Operation(
        summary = "Actualizar ciudad", 
        description = "Modifica los datos de una ciudad existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Concepción Centro\", \"regionId\": 8}"
                )
            )
        )
    )
    public ResponseEntity<Ciudad> modificarCiudad(
            @Parameter(description = "ID de la ciudad a modificar", example = "1") 
            @PathVariable Long id, 
            @RequestBody Ciudad ciudad) {
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
    @Operation(summary = "Eliminar ciudad", description = "Elimina permanentemente una ciudad del sistema")
    public ResponseEntity<Void> eliminarCiudad(
            @Parameter(description = "ID de la ciudad a eliminar", example = "1") 
            @PathVariable Long id) {
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
