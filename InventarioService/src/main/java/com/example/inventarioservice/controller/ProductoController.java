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

import com.example.inventarioservice.model.Producto;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;

@RestController
@RequestMapping("/inventario/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final TallaRepository tallaRepository;

    public ProductoController(ProductoRepository productoRepository, TallaRepository tallaRepository) {
        this.productoRepository = productoRepository;
        this.tallaRepository = tallaRepository;
    }

    @GetMapping
    public List<Producto> list() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        Producto saved = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Servir imagenes si es necesario
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> getImagen(@PathVariable Long id) {
        var opt = productoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Producto p = opt.get();
        byte[] img = p.getImagen();
        if (img == null) return ResponseEntity.notFound().build();
        String filename = p.getImagenUrl() != null ? p.getImagenUrl() : "image";
        MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) mt = MediaType.IMAGE_JPEG;
        else if (filename.endsWith(".png")) mt = MediaType.IMAGE_PNG;
        else if (filename.endsWith(".webp")) mt = MediaType.parseMediaType("image/webp");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(mt).body(img);
    }

    @GetMapping("/{id}/tallas")
    public ResponseEntity<java.util.Set<Talla>> getTallas(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(p -> ResponseEntity.ok(p.getTallas()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/tallas/{tallaId}")
    public ResponseEntity<Void> addTalla(@PathVariable Long id, @PathVariable Long tallaId) {
        var popt = productoRepository.findById(id);
        if (popt.isEmpty()) return ResponseEntity.notFound().build();
        var topt = tallaRepository.findById(tallaId);
        if (topt.isEmpty()) return ResponseEntity.notFound().build();
        Producto p = popt.get();
        p.getTallas().add(topt.get());
        productoRepository.save(p);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/tallas/unlink/{tallaId}")
    public ResponseEntity<Void> removeTalla(@PathVariable Long id, @PathVariable Long tallaId) {
        var popt = productoRepository.findById(id);
        if (popt.isEmpty()) return ResponseEntity.notFound().build();
        Producto p = popt.get();
        p.getTallas().removeIf(t -> t.getId().equals(tallaId));
        productoRepository.save(p);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
