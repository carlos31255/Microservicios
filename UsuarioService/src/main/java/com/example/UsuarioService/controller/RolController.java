package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.RolDTO;
import com.example.UsuarioService.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Roles.
 * Endpoints para CRUD de roles del sistema.
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Roles", description = "API para gestión de roles del sistema")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    @Operation(summary = "Obtener todos los roles", description = "Lista todos los roles disponibles en el sistema")
    public ResponseEntity<List<RolDTO>> obtenerTodosLosRoles() {
        List<RolDTO> roles = rolService.obtenerTodosLosRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID", description = "Busca un rol específico por su ID")
    public ResponseEntity<RolDTO> obtenerRolPorId(@PathVariable Long id) {
        RolDTO rol = rolService.obtenerRolPorId(id);
        if (rol != null) {
            return ResponseEntity.ok(rol);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nombre/{nombreRol}")
    @Operation(summary = "Obtener rol por nombre", description = "Busca un rol por su nombre exacto")
    public ResponseEntity<RolDTO> obtenerRolPorNombre(@PathVariable String nombreRol) {
        RolDTO rol = rolService.obtenerRolPorNombre(nombreRol);
        if (rol != null) {
            return ResponseEntity.ok(rol);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema")
    public ResponseEntity<RolDTO> crearRol(@RequestBody RolDTO rolDTO) {
        try {
            RolDTO nuevoRol = rolService.crearRol(rolDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza los datos de un rol existente")
    public ResponseEntity<RolDTO> actualizarRol(@PathVariable Long id, @RequestBody RolDTO rolDTO) {
        RolDTO rolActualizado = rolService.actualizarRol(id, rolDTO);
        if (rolActualizado != null) {
            return ResponseEntity.ok(rolActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        boolean eliminado = rolService.eliminarRol(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
