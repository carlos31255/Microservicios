package com.example.geografiaservice.controller;

import com.example.geografiaservice.dto.ComunaDTO;
import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.service.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geografia/comunas")
@Tag(name = "Comunas", description = "API para gestión de divisiones administrativas (comunas)")
public class ComunaController {


    private final ComunaService comunaService;

    public ComunaController(ComunaService comunaService) {
        this.comunaService = comunaService;
    }

    //Obtener todas las comunas
    @GetMapping
    @Operation(summary = "Listar todas las comunas", description = "Devuelve el listado completo de comunas disponibles en el sistema")
    public ResponseEntity<List<ComunaDTO>> obtenerTodasLasComunas() {
        try {
            List<ComunaDTO> comunas = comunaService.obtenerTodasLasComunas();
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comuna por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener comuna por ID", description = "Busca los detalles de una comuna específica mediante su identificador único")
    public ResponseEntity<ComunaDTO> obtenerComunaPorId(
            @Parameter(description = "ID único de la comuna", example = "1") 
            @PathVariable Long id) {
        try {
            ComunaDTO comuna = comunaService.obtenerComunaPorId(id);
            return ResponseEntity.ok(comuna);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por región
    @GetMapping("/region/{regionId}")
    @Operation(summary = "Listar comunas por Región", description = "Obtiene todas las comunas asociadas a una región específica")
    public ResponseEntity<List<ComunaDTO>> obtenerComunasPorRegion(
            @Parameter(description = "ID de la región", example = "13") 
            @PathVariable Long regionId) {
        try {
            List<ComunaDTO> comunas = comunaService.obtenerComunasPorRegion(regionId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por ciudad
    @GetMapping("/ciudad/{ciudadId}")
    @Operation(summary = "Listar comunas por Ciudad", description = "Obtiene todas las comunas que pertenecen a una ciudad específica")
    public ResponseEntity<List<ComunaDTO>> obtenerComunasPorCiudad(
            @Parameter(description = "ID de la ciudad", example = "5") 
            @PathVariable Long ciudadId) {
        try {
            List<ComunaDTO> comunas = comunaService.obtenerComunasPorCiudad(ciudadId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Obtener comunas por región y ciudad
    @GetMapping("/region/{regionId}/ciudad/{ciudadId}")
    @Operation(summary = "Listar comunas por Región y Ciudad", description = "Filtro combinado para obtener comunas")
    public ResponseEntity<List<ComunaDTO>> obtenerComunasPorRegionYCiudad(
            @Parameter(description = "ID de la región", example = "5") 
            @PathVariable Long regionId,
            @Parameter(description = "ID de la ciudad", example = "2") 
            @PathVariable Long ciudadId) {
        try {
            List<ComunaDTO> comunas = comunaService.obtenerComunasPorRegionYCiudad(regionId, ciudadId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Buscar comunas por nombre
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nombre", description = "Búsqueda de comunas por coincidencia de texto")
    public ResponseEntity<List<ComunaDTO>> buscarComunasPorNombre(
            @Parameter(description = "Nombre o parte del nombre de la comuna", example = "Providencia") 
            @RequestParam String nombre) {
        try {
            List<ComunaDTO> comunas = comunaService.buscarComunasPorNombre(nombre);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Crear nueva comuna
    @PostMapping("/crear")
    @Operation(
        summary = "Crear nueva comuna", 
        description = "Registra una nueva comuna en la base de datos",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva comuna",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Providencia\", \"ciudadId\": 1, \"regionId\": 13}"
                )
            )
        )
    )
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
    @Operation(
        summary = "Actualizar comuna", 
        description = "Modifica los datos de una comuna existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Providencia Norte\", \"ciudadId\": 1, \"regionId\": 13}"
                )
            )
        )
    )
    public ResponseEntity<Comuna> modificarComuna(
            @Parameter(description = "ID de la comuna a modificar", example = "1") 
            @PathVariable Long id, 
            @RequestBody Comuna comuna) {
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
    @Operation(summary = "Eliminar comuna", description = "Elimina permanentemente una comuna del sistema")
    public ResponseEntity<Void> eliminarComuna(
            @Parameter(description = "ID de la comuna a eliminar", example = "1") 
            @PathVariable Long id) {
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
