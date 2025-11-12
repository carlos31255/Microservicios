package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.CiudadDTO;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.CiudadRepository;
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
public class CiudadServiceTest {

    @Mock
    private CiudadRepository ciudadRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private CiudadService ciudadService;

    @Test
    void testObtenerTodasLasCiudades() {
        Region region = new Region();
        region.setId(13L);
        region.setCodigo("RM");

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);

        when(ciudadRepository.findAll()).thenReturn(Arrays.asList(ciudad));

        List<CiudadDTO> resultado = ciudadService.obtenerTodasLasCiudades();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Santiago");
        assertThat(resultado.get(0).getRegionId()).isEqualTo(13L);
    }

    @Test
    void testObtenerCiudadPorId() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);

        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(ciudad));

        CiudadDTO resultado = ciudadService.obtenerCiudadPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Santiago");
    }

    @Test
    void testObtenerCiudadesPorRegion() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setId(1L);
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);

        when(ciudadRepository.findByRegionIdOrderByNombreAsc(13L)).thenReturn(Arrays.asList(ciudad));

        List<CiudadDTO> resultado = ciudadService.obtenerCiudadesPorRegion(13L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRegionId()).isEqualTo(13L);
    }

    @Test
    void testGuardarCiudad() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);

        Ciudad ciudadGuardada = new Ciudad();
        ciudadGuardada.setId(1L);
        ciudadGuardada.setNombre("Santiago");
        ciudadGuardada.setRegion(region);

        when(regionRepository.existsById(13L)).thenReturn(true);
        when(ciudadRepository.existsByNombreIgnoreCaseAndRegionId("Santiago", 13L)).thenReturn(false);
        when(ciudadRepository.save(any(Ciudad.class))).thenReturn(ciudadGuardada);

        Ciudad resultado = ciudadService.guardarCiudad(ciudad);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Santiago");
    }

    @Test
    void testModificarCiudad() {
        Region region = new Region();
        region.setId(13L);

        Ciudad ciudadExistente = new Ciudad();
        ciudadExistente.setId(1L);
        ciudadExistente.setNombre("Santiago");
        ciudadExistente.setRegion(region);

        Ciudad ciudadActualizada = new Ciudad();
        ciudadActualizada.setNombre("Santiago Centro");
        ciudadActualizada.setRegion(region);

        when(ciudadRepository.findById(1L)).thenReturn(Optional.of(ciudadExistente));
        when(regionRepository.existsById(13L)).thenReturn(true);
        when(ciudadRepository.save(any(Ciudad.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ciudad resultado = ciudadService.modificarCiudad(1L, ciudadActualizada);

        assertThat(resultado.getNombre()).isEqualTo("Santiago Centro");
    }

    @Test
    void testEliminarCiudad() {
        when(ciudadRepository.existsById(1L)).thenReturn(true);

        ciudadService.eliminarCiudad(1L);

        verify(ciudadRepository).deleteById(1L);
    }
}
