package com.example.UsuarioService.controller;

import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.ClienteRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Endpoints para estadísticas y reportes del sistema de usuarios")
public class ReporteController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/estadisticas-generales")
    @Operation(summary = "Obtener estadísticas generales del sistema",
               description = "Retorna contadores de personas, clientes y usuarios del sistema")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Contadores totales
        estadisticas.put("totalPersonas", personaRepository.count());
        estadisticas.put("totalClientes", clienteRepository.count());
        estadisticas.put("totalUsuarios", usuarioRepository.count());
        
        // Contadores por estado (activos/inactivos)
        estadisticas.put("personasActivas", personaRepository.countByEstado("activo"));
        estadisticas.put("personasInactivas", personaRepository.countByEstado("inactivo"));
        estadisticas.put("clientesActivos", clienteRepository.countByActivoTrue());
        estadisticas.put("clientesInactivos", clienteRepository.countByActivoFalse());
        estadisticas.put("usuariosActivos", usuarioRepository.countByActivoTrue());
        estadisticas.put("usuariosInactivos", usuarioRepository.countByActivoFalse());
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/clientes/estadisticas")
    @Operation(summary = "Estadísticas de clientes",
               description = "Retorna información detallada sobre los clientes del sistema")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasClientes() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        long totalClientes = clienteRepository.count();
        long clientesActivos = clienteRepository.countByActivoTrue();
        
        estadisticas.put("totalClientes", totalClientes);
        estadisticas.put("clientesActivos", clientesActivos);
        estadisticas.put("clientesInactivos", totalClientes - clientesActivos);
        estadisticas.put("porcentajeActivos", 
            totalClientes > 0 ? (clientesActivos * 100.0 / totalClientes) : 0);
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/usuarios/estadisticas")
    @Operation(summary = "Estadísticas de usuarios del sistema",
               description = "Retorna información detallada sobre los usuarios administrativos")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasUsuarios() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        long totalUsuarios = usuarioRepository.count();
        long usuariosActivos = usuarioRepository.countByActivoTrue();
        
        estadisticas.put("totalUsuarios", totalUsuarios);
        estadisticas.put("usuariosActivos", usuariosActivos);
        estadisticas.put("usuariosInactivos", totalUsuarios - usuariosActivos);
        estadisticas.put("porcentajeActivos", 
            totalUsuarios > 0 ? (usuariosActivos * 100.0 / totalUsuarios) : 0);
        
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/personas/por-estado")
    @Operation(summary = "Distribución de personas por estado",
               description = "Retorna la cantidad de personas activas e inactivas")
    public ResponseEntity<Map<String, Long>> obtenerDistribucionPorEstado() {
        Map<String, Long> distribucion = new HashMap<>();
        
        distribucion.put("activas", personaRepository.countByEstado("activo"));
        distribucion.put("inactivas", personaRepository.countByEstado("inactivo"));
        
        return ResponseEntity.ok(distribucion);
    }
}
