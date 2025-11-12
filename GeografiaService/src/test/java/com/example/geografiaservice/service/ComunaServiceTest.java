package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.ComunaDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.ComunaRepository;
import com.example.geografiaservice.repository.RegionRepository;
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
public class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private CiudadRepository ciudadRepository;

    @InjectMocks
    private ComunaService comunaService;

    @Test
    void testObtenerTodasLasComunas() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaRepository.findAll()).thenReturn(Arrays.asList(comuna));

        List<ComunaDTO> resultado = comunaService.obtenerTodasLasComunas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Providencia");
        assertThat(resultado.get(0).getRegionId()).isEqualTo(13L);
    }

    @Test
    void testObtenerComunaPorId() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comuna));

        ComunaDTO resultado = comunaService.obtenerComunaPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Providencia");
    }

    @Test
    void testObtenerComunasPorRegion() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaRepository.findByRegionIdOrderByNombreAsc(13L)).thenReturn(Arrays.asList(comuna));

        List<ComunaDTO> resultado = comunaService.obtenerComunasPorRegion(13L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRegionId()).isEqualTo(13L);
    }

    @Test
    void testObtenerComunasPorCiudad() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaRepository.findByCiudadIdOrderByNombreAsc(1L)).thenReturn(Arrays.asList(comuna));

        List<ComunaDTO> resultado = comunaService.obtenerComunasPorCiudad(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCiudadId()).isEqualTo(1L);
    }

    @Test
    void testObtenerComunasPorRegionYCiudad() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setId(1L);
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        when(comunaRepository.findByRegionIdAndCiudadId(13L, 1L)).thenReturn(Arrays.asList(comuna));

        List<ComunaDTO> resultado = comunaService.obtenerComunasPorRegionYCiudad(13L, 1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRegionId()).isEqualTo(13L);
        assertThat(resultado.get(0).getCiudadId()).isEqualTo(1L);
    }

    @Test
    void testGuardarComuna() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comuna = new Comuna();
        comuna.setNombre("Providencia");
        comuna.setRegion(region);
        comuna.setCiudad(ciudad);

        Comuna comunaGuardada = new Comuna();
        comunaGuardada.setId(1L);
        comunaGuardada.setNombre("Providencia");
        comunaGuardada.setRegion(region);
        comunaGuardada.setCiudad(ciudad);

        when(regionRepository.existsById(13L)).thenReturn(true);
        when(ciudadRepository.existsById(1L)).thenReturn(true);
        when(comunaRepository.existsByNombreIgnoreCaseAndCiudadId("Providencia", 1L)).thenReturn(false);
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comunaGuardada);

        Comuna resultado = comunaService.guardarComuna(comuna);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Providencia");
    }

    @Test
    void testModificarComuna() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);

        Comuna comunaExistente = new Comuna();
        comunaExistente.setId(1L);
        comunaExistente.setNombre("Providencia");
        comunaExistente.setRegion(region);
        comunaExistente.setCiudad(ciudad);

        Comuna comunaActualizada = new Comuna();
        comunaActualizada.setNombre("Providencia Centro");
        comunaActualizada.setRegion(region);
        comunaActualizada.setCiudad(ciudad);

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaExistente));
        when(regionRepository.existsById(13L)).thenReturn(true);
        when(ciudadRepository.existsById(1L)).thenReturn(true);
        when(comunaRepository.save(any(Comuna.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comuna resultado = comunaService.modificarComuna(1L, comunaActualizada);

        assertThat(resultado.getNombre()).isEqualTo("Providencia Centro");
    }

    @Test
    void testEliminarComuna() {
        when(comunaRepository.existsById(1L)).thenReturn(true);

        comunaService.eliminarComuna(1L);

        verify(comunaRepository).deleteById(1L);
    }
}
