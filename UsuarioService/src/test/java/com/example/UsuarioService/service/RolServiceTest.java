package com.example.UsuarioService.service;

import com.example.UsuarioService.dto.RolDTO;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @Test
    void testObtenerTodosLosRoles() {
        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");
        rol.setDescripcion("Rol de administrador");

        when(rolRepository.findAll()).thenReturn(Arrays.asList(rol));

        List<RolDTO> resultado = rolService.obtenerTodosLosRoles();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreRol()).isEqualTo("Administrador");
        assertThat(resultado.get(0).getDescripcion()).isEqualTo("Rol de administrador");
    }

    @Test
    void testObtenerRolPorId() {
        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol("Administrador");
        rol.setDescripcion("Rol de administrador");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        RolDTO resultado = rolService.obtenerRolPorId(1L);

        assertThat(resultado.getIdRol()).isEqualTo(1L);
        assertThat(resultado.getNombreRol()).isEqualTo("Administrador");
    }

    @Test
    void testObtenerRolPorNombre() {
        Rol rol = new Rol();
        rol.setIdRol(2L);
        rol.setNombreRol("Transportista");

        when(rolRepository.findByNombreRol("Transportista")).thenReturn(Optional.of(rol));

        RolDTO resultado = rolService.obtenerRolPorNombre("Transportista");

        assertThat(resultado.getIdRol()).isEqualTo(2L);
        assertThat(resultado.getNombreRol()).isEqualTo("Transportista");
    }

    @Test
    void testCrearRol() {
        RolDTO rolDTO = new RolDTO();
        rolDTO.setNombreRol("Cliente");
        rolDTO.setDescripcion("Rol de cliente");

        Rol rolGuardado = new Rol();
        rolGuardado.setIdRol(3L);
        rolGuardado.setNombreRol("Cliente");
        rolGuardado.setDescripcion("Rol de cliente");

        when(rolRepository.save(any(Rol.class))).thenReturn(rolGuardado);

        RolDTO resultado = rolService.crearRol(rolDTO);

        assertThat(resultado.getIdRol()).isEqualTo(3L);
        assertThat(resultado.getNombreRol()).isEqualTo("Cliente");
        assertThat(resultado.getDescripcion()).isEqualTo("Rol de cliente");
    }

    @Test
    void testActualizarRol() {
        Rol rolExistente = new Rol();
        rolExistente.setIdRol(1L);
        rolExistente.setNombreRol("Administrador");

        RolDTO rolDTO = new RolDTO();
        rolDTO.setNombreRol("Admin Modificado");
        rolDTO.setDescripcion("Nueva descripción");

        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolExistente));
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RolDTO resultado = rolService.actualizarRol(1L, rolDTO);

        assertThat(resultado.getNombreRol()).isEqualTo("Admin Modificado");
        assertThat(resultado.getDescripcion()).isEqualTo("Nueva descripción");
    }

    @Test
    void testEliminarRol() {
        when(rolRepository.existsById(1L)).thenReturn(true);

        boolean resultado = rolService.eliminarRol(1L);

        assertThat(resultado).isTrue();
        verify(rolRepository).deleteById(1L);
    }
}
