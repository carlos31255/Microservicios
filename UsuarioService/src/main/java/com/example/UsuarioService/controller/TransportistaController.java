package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.TransportistaDTO;
import com.example.UsuarioService.service.TransportistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportistas")
@CrossOrigin(origins = "*")
@Tag(name = "Transportistas", description = "API para gestión de transportistas")
public class TransportistaController {

    @Autowired
    private TransportistaService transportistaService;

    @GetMapping
    @Operation(summary = "Obtener todos los transportistas")
    public ResponseEntity<List<TransportistaDTO>> obtenerTodos() {
        return ResponseEntity.ok(transportistaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transportista por ID")
    public ResponseEntity<TransportistaDTO> obtenerPorId(@PathVariable Long id) {
        TransportistaDTO t = transportistaService.obtenerPorId(id);
        if (t != null) return ResponseEntity.ok(t);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/persona/{personaId}")
    @Operation(summary = "Obtener transportista por personaId")
    public ResponseEntity<TransportistaDTO> obtenerPorPersona(@PathVariable Long personaId) {
        TransportistaDTO t = transportistaService.obtenerPorPersonaId(personaId);
        if (t != null) return ResponseEntity.ok(t);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear transportista")
    public ResponseEntity<TransportistaDTO> crear(@RequestBody TransportistaDTO dto) {
        try {
            TransportistaDTO creado = transportistaService.crearTransportista(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar transportista")
    public ResponseEntity<TransportistaDTO> actualizar(@PathVariable Long id, @RequestBody TransportistaDTO dto) {
        TransportistaDTO actualizado = transportistaService.actualizarTransportista(id, dto);
        if (actualizado != null) return ResponseEntity.ok(actualizado);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transportista")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = transportistaService.eliminarTransportista(id);
        if (eliminado) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
