package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.UsuarioDTO;
import com.example.UsuarioService.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Usuarios.
 * Endpoints para CRUD y operaciones de usuarios (relación Persona-Rol).
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "API para gestión de usuarios (relación Persona-Rol)")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Lista todos los usuarios con sus datos de persona y rol")
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{idPersona}")
    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario por su ID de persona")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long idPersona) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(idPersona);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rol/{idRol}")
    @Operation(summary = "Obtener usuarios por rol", description = "Lista todos los usuarios que tienen un rol específico")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRol(@PathVariable Long idRol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(idRol);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario asignando un rol a una persona existente")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idPersona}/rol")
    @Operation(summary = "Actualizar rol de usuario", description = "Cambia el rol asignado a un usuario")
    public ResponseEntity<UsuarioDTO> actualizarRolUsuario(
            @PathVariable Long idPersona,
            @RequestParam Long nuevoIdRol) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarRolUsuario(idPersona, nuevoIdRol);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idPersona}")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario (borrado lógico - marca como inactivo)")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long idPersona) {
        boolean eliminado = usuarioService.eliminarUsuario(idPersona);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
