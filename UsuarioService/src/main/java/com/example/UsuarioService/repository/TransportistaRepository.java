package com.example.UsuarioService.repository;

import com.example.UsuarioService.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Long> {
    Optional<Transportista> findByIdTransportista(Long idTransportista);
    Optional<Transportista> findByIdPersona(Long idPersona);
    List<Transportista> findByTipoVehiculo(String tipoVehiculo);
    Optional<Transportista> findByPatente(String patente);
}
