package com.example.UsuarioService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "usuario",
    indexes = {
        @Index(name = "idx_usuario_persona", columnList = "id_persona", unique = true),
        @Index(name = "idx_usuario_rol", columnList = "id_rol")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @Column(name = "id_persona")
    private Long idPersona;

    @Column(name = "id_rol", nullable = false)
    private Long idRol;

    @Column(nullable = false)
    private Boolean activo = true; // Para borrado lógico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", insertable = false, updatable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private Rol rol;

    // Constructor simple para crear usuarios
    public Usuario(Long idPersona, Long idRol) {
        this.idPersona = idPersona;
        this.idRol = idRol;
        this.activo = true;
    }
}
