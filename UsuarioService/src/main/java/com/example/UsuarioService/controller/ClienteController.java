package com.example.UsuarioService.controller;

import com.example.UsuarioService.dto.ClienteDTO;
import com.example.UsuarioService.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Lista todos los clientes con sus datos de persona")
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{idPersona}")
    @Operation(summary = "Obtener cliente por ID", description = "Busca un cliente por su ID de persona")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long idPersona) {
        ClienteDTO cliente = clienteService.obtenerClientePorId(idPersona);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener clientes por categoría", description = "Lista clientes filtrados por categoría (VIP, premium, regular)")
    public ResponseEntity<List<ClienteDTO>> obtenerClientesPorCategoria(@PathVariable String categoria) {
        List<ClienteDTO> clientes = clienteService.obtenerClientesPorCategoria(categoria);
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente asociado a una persona existente")
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO nuevoCliente = clienteService.crearCliente(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{idPersona}/categoria")
    @Operation(summary = "Actualizar categoría de cliente", description = "Cambia la categoría de un cliente (VIP, premium, regular)")
    public ResponseEntity<ClienteDTO> actualizarCategoria(
            @PathVariable Long idPersona,
            @RequestParam String nuevaCategoria) {
        ClienteDTO clienteActualizado = clienteService.actualizarCategoria(idPersona, nuevaCategoria);
        if (clienteActualizado != null) {
            return ResponseEntity.ok(clienteActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idPersona}")
    @Operation(summary = "Desactivar cliente", description = "Desactiva un cliente (borrado lógico - marca como inactivo)")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long idPersona) {
        boolean eliminado = clienteService.eliminarCliente(idPersona);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
