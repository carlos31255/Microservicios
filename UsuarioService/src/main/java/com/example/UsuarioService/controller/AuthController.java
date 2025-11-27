package com.example.UsuarioService.controller;

import com.example.UsuarioService.auth.AuthRequest;
import com.example.UsuarioService.auth.AuthResponse;
import com.example.UsuarioService.dto.PersonaDTO;
import com.example.UsuarioService.dto.UsuarioDTO;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.repository.RolRepository;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.service.PersonaService;
import com.example.UsuarioService.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Endpoints de registro y login")
public class AuthController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @PostMapping("/register")
    @Operation(
        summary = "Registro de usuario", 
        description = "Crea una persona y su usuario asociado (rol por defecto: Cliente)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la persona a registrar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"username\": \"juanperez\", \"password\": \"secret123\", \"nombre\": \"Juan\", \"apellido\": \"Perez\", \"email\": \"juan@example.com\", \"telefono\": \"+56912345678\"}"
                )
            )
        )
    )
    public ResponseEntity<?> register(
            @RequestBody PersonaDTO personaDTO, 
            @Parameter(description = "Rol a asignar (opcional, por defecto Cliente)", example = "Vendedor") 
            @RequestParam(required = false) String rol) {
        try {
            // Crear persona (hashea contraseña internamente)
            PersonaDTO creada = personaService.crearPersona(personaDTO);

            // Encontrar rol por nombre, por defecto "Cliente"
            String rolNombre = (rol == null || rol.isEmpty()) ? "Cliente" : rol;
            Optional<Rol> rolOpt = rolRepository.findByNombreRol(rolNombre);
            if (rolOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no encontrado: " + rolNombre);
            }

            // Crear usuario asociado
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setIdPersona(creada.getIdPersona());
            usuarioDTO.setIdRol(rolOpt.get().getIdRol());
            UsuarioDTO creadoUsuario = usuarioService.crearUsuario(usuarioDTO);

            AuthResponse resp = new AuthResponse();
            resp.setIdPersona(creada.getIdPersona());
            resp.setUsername(creada.getUsername());
            resp.setNombreCompleto(creada.getNombre() + " " + creada.getApellido());
            resp.setRol(rolOpt.get().getNombreRol());
            resp.setMessage("Registro exitoso");
            // llenar campos adicionales: idRol, activo, email, telefono
            if (creadoUsuario != null) {
                resp.setIdRol(creadoUsuario.getIdRol());
                resp.setActivo(creadoUsuario.getActivo());
            } else {
                resp.setActivo(true);
            }
            resp.setEmail(creada.getEmail());
            resp.setTelefono(creada.getTelefono());

            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en registro");
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login", 
        description = "Verifica credenciales y retorna información básica del usuario",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales de acceso",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"username\": \"juanperez\", \"password\": \"secret123\"}"
                )
            )
        )
    )
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        PersonaDTO persona = personaService.verificarCredenciales(req.getUsername(), req.getPassword());
        if (persona == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // Intentar obtener usuario completo
        Optional<com.example.UsuarioService.model.Usuario> usuarioOpt = usuarioRepository.findByIdPersonaWithPersonaAndRol(persona.getIdPersona());
        String nombreRol = "";
        if (usuarioOpt.isPresent() && usuarioOpt.get().getRol() != null) {
            nombreRol = usuarioOpt.get().getRol().getNombreRol();
        }

        AuthResponse resp = new AuthResponse();
        resp.setIdPersona(persona.getIdPersona());
        resp.setUsername(persona.getUsername());
        resp.setNombreCompleto(persona.getNombre() + " " + persona.getApellido());
        resp.setRol(nombreRol);
        resp.setMessage("Login exitoso");
        // completar campos adicionales desde usuarioOpt y persona
        if (usuarioOpt.isPresent()) {
            com.example.UsuarioService.model.Usuario u = usuarioOpt.get();
            resp.setIdRol(u.getIdRol());
            resp.setActivo(u.getActivo());
            if (u.getPersona() != null) {
                resp.setEmail(u.getPersona().getEmail());
                resp.setTelefono(u.getPersona().getTelefono());
            } else {
                resp.setEmail(persona.getEmail());
                resp.setTelefono(persona.getTelefono());
            }
        } else {
            resp.setEmail(persona.getEmail());
            resp.setTelefono(persona.getTelefono());
            resp.setActivo(true);
        }

        return ResponseEntity.ok(resp);
    }
}
