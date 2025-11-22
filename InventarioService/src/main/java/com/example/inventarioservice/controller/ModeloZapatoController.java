package com.example.inventarioservice.controller;

import org.springframework.http.HttpStatus;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventarioservice.model.ModeloZapato;
import com.example.inventarioservice.repository.ModeloZapatoRepository;

@RestController
@RequestMapping("/api/modelos")
public class ModeloZapatoController {

    private final ModeloZapatoRepository modeloRepository;

    public ModeloZapatoController(ModeloZapatoRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    @GetMapping
    public List<ModeloZapato> list() {
        return modeloRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloZapato> getById(@PathVariable Long id) {
        return modeloRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<ModeloZapato> create(@RequestBody ModeloZapato modelo) {
        ModeloZapato saved = modeloRepository.save(modelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Opcional: servir la imagen desde la base de datos si existe
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> getImagen(@PathVariable Long id) {
        var opt = modeloRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        ModeloZapato m = opt.get();
        byte[] img = m.getImagen();
        if (img == null) return ResponseEntity.notFound().build();
        String filename = m.getImagenUrl() != null ? m.getImagenUrl() : "image";
        MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) mt = MediaType.IMAGE_JPEG;
        else if (filename.endsWith(".png")) mt = MediaType.IMAGE_PNG;
        else if (filename.endsWith(".webp")) mt = MediaType.parseMediaType("image/webp");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(mt).body(img);
    }
}
