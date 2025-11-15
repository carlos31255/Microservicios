package com.example.ventasservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalles_boleta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleBoleta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "id_boleta", nullable = false)
    private Integer boletaId;
    
    @Column(name = "id_inventario", nullable = false)
    private Integer inventarioId;
    
    @Column(name = "nombre_producto", nullable = false, length = 200)
    private String nombreProducto;
    
    @Column(nullable = false, length = 50)
    private String talla;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;
    
    @Column(nullable = false)
    private Integer subtotal;
    
    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = cantidad * precioUnitario;
        }
    }
}
