package com.example.inventarioservice.controller;

import org.springframework.http.HttpStatus;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventarioservice.model.Producto;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/inventario/productos")
@Tag(name = "Productos", description = "Catálogo de productos y gestión de imágenes")
public class ProductoController {
    @Autowired
    private  ProductoRepository productoRepository;

    @Autowired
    private  TallaRepository tallaRepository;


    @Operation(summary = "Listar productos", description = "Obtiene el catálogo completo de productos disponibles")
    @GetMapping
    public List<Producto> list() {
        return productoRepository.findAll();
    }

    @Operation(summary = "Obtener producto por ID", description = "Busca los detalles de un producto específico")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(
            @Parameter(description = "ID del producto", example = "10") 
            @PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear producto (Solo Datos)", 
        description = "Crea un producto enviando un JSON estándar sin imagen adjunta",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo producto",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Zapatilla Running\", \"marcaId\": 1, \"descripcion\": \"Zapatilla deportiva ligera\", \"precioUnitario\": 45000}"
                )
            )
        )
    )
    @PostMapping("/crear")
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        return saveProductoSafe(producto);
    }

    // endpoint para crear producto con imagen
    @Operation(summary = "Crear producto con Imagen", description = "Crea un producto recibiendo datos y un archivo de imagen (Multipart File)")
    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> createWithImage(
            @Parameter(description = "Datos del producto en formato JSON") @RequestPart("producto") Producto producto,
            @Parameter(description = "Archivo de imagen opcional") @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        if (producto.getId() != null && producto.getId() <= 0) producto.setId(null);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                producto.setImagen(imagen.getBytes());
                String original = imagen.getOriginalFilename();
                if (original != null && !original.isBlank()) {
                    producto.setImagenUrl("/images/" + original);
                }
            } catch (java.io.IOException ioe) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return saveProductoSafe(producto);
    }

    // Actualizar producto (JSON)
    @Operation(
        summary = "Actualizar producto (Solo Datos)", 
        description = "Actualiza la información básica de un producto vía JSON",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"nombre\": \"Zapatilla Running Pro\", \"precioUnitario\": 50000}"
                )
            )
        )
    )
    @PutMapping("/editar/{id}")
    public ResponseEntity<Producto> update(
            @Parameter(description = "ID del producto a actualizar", example = "10") 
            @PathVariable Long id, 
            @RequestBody Producto producto) {
        var opt = productoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Producto existing = opt.get();

        // Actualizar solo campos básicos (NO TALLAS)
        if (producto.getNombre() != null) existing.setNombre(producto.getNombre());
        if (producto.getMarcaId() != null) existing.setMarcaId(producto.getMarcaId());
        if (producto.getDescripcion() != null) existing.setDescripcion(producto.getDescripcion());
        if (producto.getImagenUrl() != null) existing.setImagenUrl(producto.getImagenUrl());
        if (producto.getPrecioUnitario() != null) existing.setPrecioUnitario(producto.getPrecioUnitario());
        if (producto.getImagen() != null) existing.setImagen(producto.getImagen());

        // Las tallas se gestionan automáticamente por la tabla producto_talla

        Producto saved = productoRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // Actualizar producto con imagen (multipart)
    @Operation(summary = "Actualizar producto con Imagen", description = "Actualiza datos y/o imagen de un producto existente (Multipart File)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> updateWithImage(
            @Parameter(description = "ID del producto", example = "10") @PathVariable Long id,
            @Parameter(description = "Datos del producto a actualizar") @RequestPart("producto") Producto producto,
            @Parameter(description = "Nueva imagen (opcional)") @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        var opt = productoRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Producto existing = opt.get();

        // Actualizar campos básicos
        if (producto.getNombre() != null) existing.setNombre(producto.getNombre());
        if (producto.getMarcaId() != null) existing.setMarcaId(producto.getMarcaId());
        if (producto.getDescripcion() != null) existing.setDescripcion(producto.getDescripcion());
        if (producto.getImagenUrl() != null) existing.setImagenUrl(producto.getImagenUrl());
        if (producto.getPrecioUnitario() != null) existing.setPrecioUnitario(producto.getPrecioUnitario());
        // No actualizar tallas desde este endpoint (se gestionan via Inventario)

        if (imagen != null && !imagen.isEmpty()) {
            try {
                existing.setImagen(imagen.getBytes());
                String original = imagen.getOriginalFilename();
                if (original != null && !original.isBlank()) {
                    existing.setImagenUrl("/images/" + original);
                }
            } catch (java.io.IOException ioe) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Producto saved = productoRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    private ResponseEntity<Producto> saveProductoSafe(Producto producto) {
        try {
            Producto saved = productoRepository.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException | org.hibernate.StaleObjectStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Servir imagenes si es necesario
    @Operation(summary = "Descargar Imagen", description = "Recupera los bytes de la imagen almacenada en base de datos")
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> getImagen(
            @Parameter(description = "ID del producto", example = "10") 
            @PathVariable Long id) {
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

    @Operation(summary = "Listar Tallas", description = "Devuelve las tallas asociadas a un producto")
    @GetMapping("/{id}/tallas")
    public ResponseEntity<java.util.Set<Talla>> getTallas(
            @Parameter(description = "ID del producto", example = "10") 
            @PathVariable Long id) {
        return productoRepository.findById(id)
                .map(p -> ResponseEntity.ok(p.getTallas()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Asignar Talla", description = "Vincula una talla existente a un producto")
    @PostMapping("/{id}/tallas/{tallaId}")
    public ResponseEntity<Void> addTalla(
            @Parameter(description = "ID del producto", example = "10") @PathVariable Long id,
            @Parameter(description = "ID de la talla a agregar", example = "3") @PathVariable Long tallaId) {
        var popt = productoRepository.findById(id);
        if (popt.isEmpty()) return ResponseEntity.notFound().build();
        var topt = tallaRepository.findById(tallaId);
        if (topt.isEmpty()) return ResponseEntity.notFound().build();
        Producto p = popt.get();
        p.getTallas().add(topt.get());
        productoRepository.save(p);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Desvincular Talla", description = "Elimina la asociación entre una talla y un producto")
    @PostMapping("/{id}/tallas/unlink/{tallaId}")
    public ResponseEntity<Void> removeTalla(
            @Parameter(description = "ID del producto", example = "10") @PathVariable Long id,
            @Parameter(description = "ID de la talla a remover", example = "3") @PathVariable Long tallaId) {
        var popt = productoRepository.findById(id);
        if (popt.isEmpty()) return ResponseEntity.notFound().build();
        Producto p = popt.get();
        p.getTallas().removeIf(t -> t.getId().equals(tallaId));
        productoRepository.save(p);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
