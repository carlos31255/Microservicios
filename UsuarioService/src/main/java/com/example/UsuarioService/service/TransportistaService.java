package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.TransportistaDTO;
import com.example.UsuarioService.model.Transportista;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.TransportistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportistaService {

    @Autowired
    private TransportistaRepository transportistaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<TransportistaDTO> obtenerTodos() {
        return transportistaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TransportistaDTO obtenerPorId(Long id) {
        return transportistaRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public TransportistaDTO obtenerPorPersonaId(Long personaId) {
        return transportistaRepository.findByIdPersona(personaId).map(this::toDTO).orElse(null);
    }

    public TransportistaDTO crearTransportista(TransportistaDTO dto) {
        if (dto.getIdPersona() == null || !personaRepository.existsById(dto.getIdPersona())) {
            throw new IllegalArgumentException("Persona indicada no existe");
        }
        Transportista t = toEntity(dto);
        t.setFechaRegistro(System.currentTimeMillis());
        Transportista saved = transportistaRepository.save(t);
        return toDTO(saved);
    }

    public TransportistaDTO actualizarTransportista(Long id, TransportistaDTO dto) {
        return transportistaRepository.findById(id).map(existing -> {
            existing.setPatente(dto.getPatente());
            existing.setTipoVehiculo(dto.getTipoVehiculo());
            existing.setActivo(dto.getActivo() != null ? dto.getActivo() : existing.getActivo());
            existing.setLicencia(dto.getLicencia() != null ? dto.getLicencia() : existing.getLicencia());
            Transportista updated = transportistaRepository.save(existing);
            return toDTO(updated);
        }).orElse(null);
    }

    public boolean eliminarTransportista(Long id) {
        if (!transportistaRepository.existsById(id)) return false;
        transportistaRepository.deleteById(id);
        return true;
    }

    private TransportistaDTO toDTO(Transportista t) {
        if (t == null) return null;
        TransportistaDTO dto = new TransportistaDTO();
        dto.setIdTransportista(t.getIdTransportista());
        dto.setIdPersona(t.getIdPersona());
        dto.setPatente(t.getPatente());
        dto.setTipoVehiculo(t.getTipoVehiculo());
        dto.setLicencia(t.getLicencia());
        dto.setActivo(t.getActivo());
        dto.setFechaRegistro(t.getFechaRegistro());
        return dto;
    }

    private Transportista toEntity(TransportistaDTO dto) {
        Transportista t = new Transportista();
        t.setIdPersona(dto.getIdPersona());
        t.setPatente(dto.getPatente());
        t.setTipoVehiculo(dto.getTipoVehiculo());
        t.setLicencia(dto.getLicencia());
        t.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        t.setFechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : System.currentTimeMillis());
        return t;
    }
}
