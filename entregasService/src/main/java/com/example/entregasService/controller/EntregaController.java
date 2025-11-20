package com.example.entregasService.controller;

import com.example.entregasService.dto.ActualizarEstadoDTO;
import com.example.entregasService.dto.AsignarTransportistaDTO;
import com.example.entregasService.dto.ContarEstadoDTO;
import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Entregas", description = "API para gestión de entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    // GET api/entregas
    @GetMapping
    public ResponseEntity<List<EntregaDTO>> listarTodas() {
        return ResponseEntity.ok(entregaService.obtenerTodasLasEntregas());
    }

    // GET api/entregas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EntregaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(entregaService.obtenerEntregaPorId(id));
    }

    // GET api/entregas/transportista/{transportistaId}
    @GetMapping("/transportista/{transportistaId}")
    public ResponseEntity<List<EntregaDTO>> listarPorTransportista(@PathVariable Integer transportistaId) {
        return ResponseEntity.ok(entregaService.getEntregasByTransportista(transportistaId));
    }

    // GET api/entregas/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EntregaDTO>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(entregaService.getEntregasByEstado(estado));
    }

    // PUT api/entregas/{id}/asignar?transportistaId=...
    @PutMapping("/{id}/asignar")
    public ResponseEntity<EntregaDTO> asignarTransportista(
            @PathVariable Integer id,
            @RequestParam Integer transportistaId) {
        return ResponseEntity.ok(entregaService.asignarTransportista(id, transportistaId));
    }

    // PUT api/entregas/{id}/completar (Body es un String simple)
    @PutMapping("/{id}/completar")
    public ResponseEntity<EntregaDTO> completarEntrega(
            @PathVariable Integer id,
            @RequestBody(required = false) String observacion) {
        return ResponseEntity.ok(entregaService.completarEntrega(id, observacion));
    }

    // PUT api/entregas/{id}/estado?nuevoEstado=...
    @PutMapping("/{id}/estado")
    public ResponseEntity<EntregaDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(entregaService.cambiarEstado(id, nuevoEstado));
    }
}
