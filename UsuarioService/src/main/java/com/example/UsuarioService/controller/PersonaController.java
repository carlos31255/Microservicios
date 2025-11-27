package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/personas")
@Tag(name = "Personas", description = "API para gestión de personas")
public class PersonaController {

    private final PersonaService personaService;

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las personas", description = "Lista todas las personas registradas")
    public ResponseEntity<List<PersonaDTO>> obtenerTodasLasPersonas() {
        List<PersonaDTO> personas = personaService.obtenerTodasLasPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por ID", description = "Busca una persona por su ID")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorId(
            @Parameter(description = "ID de la persona", example = "1") 
            @PathVariable Long id) {
        PersonaDTO persona = personaService.obtenerPersonaPorId(id);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Obtener persona por RUT", description = "Busca una persona por su RUT")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorRut(
            @Parameter(description = "RUT de la persona", example = "12345678-9") 
            @PathVariable String rut) {
        PersonaDTO persona = personaService.obtenerPersonaPorRut(rut);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener persona por username", description = "Busca una persona por su nombre de usuario")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorUsername(
            @Parameter(description = "Nombre de usuario", example = "juanperez") 
            @PathVariable String username) {
        PersonaDTO persona = personaService.obtenerPersonaPorUsername(username);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar personas por nombre", description = "Busca personas por nombre o apellido (búsqueda parcial)")
    public ResponseEntity<List<PersonaDTO>> buscarPersonasPorNombre(
            @Parameter(description = "Nombre o apellido a buscar", example = "Juan") 
            @RequestParam String nombre) {
        List<PersonaDTO> personas = personaService.buscarPersonasPorNombre(nombre);
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener personas por estado", description = "Lista personas filtradas por estado (activo/inactivo)")
    public ResponseEntity<List<PersonaDTO>> obtenerPersonasPorEstado(
            @Parameter(description = "Estado (activo/inactivo)", example = "activo") 
            @PathVariable String estado) {
        List<PersonaDTO> personas = personaService.obtenerPersonasPorEstado(estado);
        return ResponseEntity.ok(personas);
    }

   @PostMapping("/crear")
    @Operation(
        summary = "Crear nueva persona", 
        description = "Registra una nueva persona en el sistema",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva persona",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"rut\": \"12345678-9\", \"nombre\": \"Juan\", \"apellido\": \"Perez\", \"fechaNacimiento\": \"1990-01-01\", \"genero\": \"Masculino\", \"direccion\": \"Av. Principal 123\", \"telefono\": \"+56912345678\", \"email\": \"juan.perez@example.com\", \"username\": \"juanperez\", \"password\": \"secret123\", \"estado\": \"activo\"}"
                )
            )
        )
    )
    public ResponseEntity<Object> crearPersona(@RequestBody PersonaDTO personaDTO) {
        try {
            PersonaDTO nuevaPersona = personaService.crearPersona(personaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
            
        } catch (IllegalArgumentException e) {
            // Error 400
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (ResponseStatusException e) {
            // Error 409 (Conflict) u otros lanzados por Spring
            Map<String, String> response = new HashMap<>();
            String mensaje = e.getReason() != null ? e.getReason() : "Error en la solicitud";
            response.put("error", mensaje); 
            return ResponseEntity.status(e.getStatusCode()).body(response);
            
        } catch (Exception ex) {
            // Error 500
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar persona", 
        description = "Actualiza los datos de una persona existente",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Juan Modificado\", \"direccion\": \"Nueva Direccion 456\"}"
                )
            )
        )
    )
    public ResponseEntity<PersonaDTO> actualizarPersona(
            @Parameter(description = "ID de la persona", example = "1") 
            @PathVariable Long id, 
            @RequestBody PersonaDTO personaDTO) {
        PersonaDTO personaActualizada = personaService.actualizarPersona(id, personaDTO);
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar persona", description = "Desactiva una persona (borrado lógico - cambia estado a inactivo)")
    public ResponseEntity<Void> eliminarPersona(
            @Parameter(description = "ID de la persona", example = "1") 
            @PathVariable Long id) {
        boolean eliminado = personaService.eliminarPersona(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/verificar-credenciales")
    @Operation(summary = "Verificar credenciales", description = "Verifica username y contraseña para autenticación")
    public ResponseEntity<PersonaDTO> verificarCredenciales(
            @Parameter(description = "Nombre de usuario", example = "juanperez") 
            @RequestParam String username,
            @Parameter(description = "Contraseña", example = "secret123") 
            @RequestParam String password) {
        PersonaDTO persona = personaService.verificarCredenciales(username, password);
        if (persona != null) {
            return ResponseEntity.ok(persona);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}/cambiar-contrasena")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña de una persona (requiere contraseña actual)")
    public ResponseEntity<Void> cambiarContrasena(
            @Parameter(description = "ID de la persona", example = "1") 
            @PathVariable Long id,
            @Parameter(description = "Contraseña actual", example = "oldpass") 
            @RequestParam String passwordActual,
            @Parameter(description = "Nueva contraseña", example = "newpass") 
            @RequestParam String passwordNueva) {
        try {
            boolean cambiado = personaService.cambiarContrasena(id, passwordActual, passwordNueva);
            if (cambiado) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}/establecer-contrasena")
    @Operation(summary = "Establecer contraseña", description = "Establece una nueva contraseña (solo administradores)")
    public ResponseEntity<Void> establecerContrasena(
            @Parameter(description = "ID de la persona", example = "1") 
            @PathVariable Long id,
            @Parameter(description = "Nueva contraseña", example = "newpassAdmin") 
            @RequestParam String passwordNueva) {
        boolean establecido = personaService.establecerContrasena(id, passwordNueva);
        if (establecido) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
