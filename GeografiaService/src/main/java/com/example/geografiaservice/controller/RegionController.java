package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.service.RegionService;

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
@RequestMapping("/geografia/regiones")
@Tag(name = "Regiones", description = "API para gestión de divisiones administrativas mayores (regiones)")
public class RegionController {
    @Autowired
    private RegionService regionService;

    //Obtener todas las regiones
    @GetMapping
    @Operation(summary = "Listar todas las regiones", description = "Devuelve el listado completo de regiones registradas en el sistema")
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
    @Operation(summary = "Obtener región por ID", description = "Busca los detalles de una región específica mediante su identificador único")
    public ResponseEntity<RegionDTO> obtenerRegionPorId(
            @Parameter(description = "ID único de la región", example = "13") 
            @PathVariable Long id) {
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
    @Operation(summary = "Obtener región por Código", description = "Busca una región utilizando su código abreviado (ej. RM, V, VIII)")
    public ResponseEntity<RegionDTO> obtenerRegionPorCodigo(
            @Parameter(description = "Código de la región", example = "RM") 
            @PathVariable String codigo) {
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
    @Operation(
        summary = "Crear nueva región", 
        description = "Registra una nueva región administrativa en la base de datos",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva región",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Metropolitana de Santiago\", \"codigo\": \"RM\", \"numeroRomano\": \"XIII\"}"
                )
            )
        )
    )
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
    @Operation(
        summary = "Actualizar región", 
        description = "Modifica los datos de una región existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Valparaíso\", \"codigo\": \"V\", \"numeroRomano\": \"V\"}"
                )
            )
        )
    )
    public ResponseEntity<Region> modificarRegion(
            @Parameter(description = "ID de la región a modificar", example = "5") 
            @PathVariable Long id, 
            @RequestBody Region region) {
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
    @Operation(summary = "Eliminar región", description = "Elimina permanentemente una región del sistema")
    public ResponseEntity<Void> eliminarRegion(
            @Parameter(description = "ID de la región a eliminar", example = "13") 
            @PathVariable Long id) {
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
