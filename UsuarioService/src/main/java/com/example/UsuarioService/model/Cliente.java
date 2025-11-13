package com.example.UsuarioService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "cliente",
    indexes = {
        @Index(name = "idx_cliente_persona", columnList = "id_persona", unique = true)
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @Column(name = "id_persona")
    private Long idPersona;

    @Column(length = 20)
    private String categoria; // VIP, regular, premium

    @Column(nullable = false)
    private Boolean activo = true; // Para borrado lógico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", insertable = false, updatable = false)
    private Persona persona;

    // Constructor simple
    public Cliente(Long idPersona, String categoria) {
        this.idPersona = idPersona;
        this.categoria = categoria;
        this.activo = true;
    }
}
