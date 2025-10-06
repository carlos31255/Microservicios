package com.example.inventarioservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "movimientos_inventario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientosInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_inventario", nullable = false)
    private Long inventarioId;
    
    @Column(nullable = false)
    private String tipo; // "entrada" o "salida"

    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false, length = 255)
    private String motivo; // "venta", "devolución", "ajuste", etc.
    
    @Column(name = "id_usuario", nullable = true) // cambiar proximamente a false
    private Long usuarioId;


}
