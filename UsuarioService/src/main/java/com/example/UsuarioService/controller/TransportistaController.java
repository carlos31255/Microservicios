package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.TransportistaDTO;
import com.example.UsuarioService.service.TransportistaService;
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
@RequestMapping("/transportistas")
@Tag(name = "Transportistas", description = "API para gestión de transportistas")
public class TransportistaController {
    @Autowired
    private  TransportistaService transportistaService;


    @GetMapping
    @Operation(summary = "Obtener todos los transportistas", description = "Lista todos los transportistas registrados en el sistema")
    public ResponseEntity<List<TransportistaDTO>> obtenerTodos() {
        return ResponseEntity.ok(transportistaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transportista por ID", description = "Busca un transportista por su identificador único")
    public ResponseEntity<TransportistaDTO> obtenerPorId(
            @Parameter(description = "ID del transportista", example = "1") 
            @PathVariable Long id) {
        TransportistaDTO t = transportistaService.obtenerPorId(id);
        if (t != null) {
            return ResponseEntity.ok(t);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/persona/{personaId}")
    @Operation(summary = "Obtener transportista por personaId", description = "Obtiene la información de transportista asociada a una persona específica")
    public ResponseEntity<TransportistaDTO> obtenerPorPersona(
            @Parameter(description = "ID de la persona asociada", example = "10") 
            @PathVariable Long personaId) {
        TransportistaDTO t = transportistaService.obtenerPorPersonaId(personaId);
        if (t != null) {
            return ResponseEntity.ok(t);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
        summary = "Crear transportista", 
        description = "Registra un nuevo transportista en el sistema",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del transportista",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"idPersona\": 10, \"licencia\": \"A4\", \"tipoVehiculo\": \"Camión 3/4\", \"disponible\": true}"
                )
            )
        )
    )
    public ResponseEntity<TransportistaDTO> crear(@RequestBody TransportistaDTO dto) {
        try {
            TransportistaDTO creado = transportistaService.crearTransportista(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar transportista", 
        description = "Actualiza los datos de un transportista existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"licencia\": \"A5\", \"tipoVehiculo\": \"Camión Tolva\", \"disponible\": false}"
                )
            )
        )
    )
    public ResponseEntity<TransportistaDTO> actualizar(
            @Parameter(description = "ID del transportista a actualizar", example = "1") 
            @PathVariable Long id, 
            @RequestBody TransportistaDTO dto) {
        TransportistaDTO actualizado = transportistaService.actualizarTransportista(id, dto);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transportista", description = "Elimina (o desactiva) un transportista del sistema")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del transportista a eliminar", example = "1") 
            @PathVariable Long id) {
        boolean eliminado = transportistaService.eliminarTransportista(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
