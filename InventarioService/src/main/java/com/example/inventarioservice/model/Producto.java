package com.example.inventarioservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Producto (antes ModeloZapato)")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "id_marca", nullable = false)
    private Long marcaId;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Lob
    @Column(name = "imagen_blob", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @Column(name = "precio", nullable = false)
    private Integer precioUnitario;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "producto_talla",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "talla_id"))
        private java.util.Set<Talla> tallas = new java.util.HashSet<>();

}
