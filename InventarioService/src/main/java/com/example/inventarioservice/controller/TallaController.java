package com.example.inventarioservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.TallaRepository;

@RestController
@RequestMapping("/inventario/tallas")
public class TallaController {

    private final TallaRepository tallaRepository;

    public TallaController(TallaRepository tallaRepository) {
        this.tallaRepository = tallaRepository;
    }

    @GetMapping
    public List<Talla> list() {
        return tallaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Talla> getById(@PathVariable Long id) {
        return tallaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<Talla> create(@RequestBody Talla talla) {
        Talla saved = tallaRepository.save(talla);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
