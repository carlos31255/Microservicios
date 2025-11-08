package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Personas.
 * Endpoints para CRUD y operaciones específicas de personas.
 */
@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
@Tag(name = "Personas", description = "API para gestión de personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping
    @Operation(summary = "Obtener todas las personas", description = "Lista todas las personas registradas")
    public ResponseEntity<List<PersonaDTO>> obtenerTodasLasPersonas() {
        List<PersonaDTO> personas = personaService.obtenerTodasLasPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por ID", description = "Busca una persona por su ID")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorId(@PathVariable Long id) {
        PersonaDTO persona = personaService.obtenerPersonaPorId(id);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Obtener persona por RUT", description = "Busca una persona por su RUT")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorRut(@PathVariable String rut) {
        PersonaDTO persona = personaService.obtenerPersonaPorRut(rut);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener persona por username", description = "Busca una persona por su nombre de usuario")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorUsername(@PathVariable String username) {
        PersonaDTO persona = personaService.obtenerPersonaPorUsername(username);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar personas por nombre", description = "Busca personas por nombre o apellido (búsqueda parcial)")
    public ResponseEntity<List<PersonaDTO>> buscarPersonasPorNombre(@RequestParam String nombre) {
        List<PersonaDTO> personas = personaService.buscarPersonasPorNombre(nombre);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener personas por estado", description = "Lista personas filtradas por estado (activo/inactivo)")
    public ResponseEntity<List<PersonaDTO>> obtenerPersonasPorEstado(@PathVariable String estado) {
        List<PersonaDTO> personas = personaService.obtenerPersonasPorEstado(estado);
        return ResponseEntity.ok(personas);
    }

    @PostMapping
    @Operation(summary = "Crear nueva persona", description = "Registra una nueva persona en el sistema")
    public ResponseEntity<PersonaDTO> crearPersona(@RequestBody PersonaDTO personaDTO) {
        try {
            PersonaDTO nuevaPersona = personaService.crearPersona(personaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar persona", description = "Actualiza los datos de una persona existente")
    public ResponseEntity<PersonaDTO> actualizarPersona(@PathVariable Long id, @RequestBody PersonaDTO personaDTO) {
        PersonaDTO personaActualizada = personaService.actualizarPersona(id, personaDTO);
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar persona", description = "Elimina una persona del sistema")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        boolean eliminado = personaService.eliminarPersona(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/verificar-credenciales")
    @Operation(summary = "Verificar credenciales", description = "Verifica username y contraseña para autenticación")
    public ResponseEntity<PersonaDTO> verificarCredenciales(
            @RequestParam String username,
            @RequestParam String password) {
        PersonaDTO persona = personaService.verificarCredenciales(username, password);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
