package com.example.inventarioservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventarioservice.model.Marca;
import com.example.inventarioservice.repository.MarcaRepository;

// Imports de Swagger / OpenAPI
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/inventario/marcas")
@Tag(name = "Marcas", description = "Operaciones de gestión de marcas de inventario")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    public MarcaController(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Operation(summary = "Listar todas las marcas", description = "Devuelve un listado completo de las marcas disponibles")
    @GetMapping
    public List<Marca> list() {
        return marcaRepository.findAll();
    }

    @Operation(summary = "Obtener una marca por ID", description = "Busca una marca específica basada en su identificador único")
    @GetMapping("/{id}")
    public ResponseEntity<Marca> getById(
            @Parameter(description = "ID único de la marca", example = "1") 
            @PathVariable Long id) {
        return marcaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear una nueva marca", 
        description = "Guarda una nueva marca en la base de datos",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la marca a crear",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Nike\", \"descripcion\": \"Marca deportiva internacional\"}"
                )
            )
        )
    )
    @PostMapping("/crear")
    public ResponseEntity<Marca> create(@RequestBody Marca marca) {
        Marca saved = marcaRepository.save(marca);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
