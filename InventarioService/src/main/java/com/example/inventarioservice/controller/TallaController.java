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

import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.TallaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/inventario/tallas")
@Tag(name = "Tallas", description = "Catálogo de tallas disponibles para productos")
public class TallaController {
    @Autowired
    private TallaRepository tallaRepository;


    @Operation(summary = "Listar todas las tallas", description = "Devuelve el listado completo de tallas registradas (ej. S, M, L, 40, 42)")
    @GetMapping
    public List<Talla> list() {
        return tallaRepository.findAll();
    }
    @Operation(summary = "Obtener talla por ID", description = "Busca una talla específica mediante su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<Talla> getById(
            @Parameter(description = "ID de la talla", example = "1") 
            @PathVariable Long id) {
        return tallaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nueva talla", 
        description = "Registra una nueva talla en el sistema",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva talla",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"XL\"}"
                )
            )
        )
    )
    @PostMapping("/crear")
    public ResponseEntity<Talla> create(@RequestBody Talla talla) {
        Talla saved = tallaRepository.save(talla);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
