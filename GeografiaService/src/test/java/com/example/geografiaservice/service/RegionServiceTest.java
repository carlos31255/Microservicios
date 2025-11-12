package com.example.geografiaservice.service;

import com.example.geografiaservice.dto.RegionDTO;
import com.example.geografiaservice.model.Region;
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
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void testObtenerTodasLasRegiones() {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");
        region.setOrden(13);

        when(regionRepository.findAllByOrderByOrdenAsc()).thenReturn(Arrays.asList(region));

        List<RegionDTO> resultado = regionService.obtenerTodasLasRegiones();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Región Metropolitana");
        assertThat(resultado.get(0).getCodigo()).isEqualTo("RM");
    }

    @Test
    void testObtenerRegionPorId() {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");

        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        RegionDTO resultado = regionService.obtenerRegionPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Región Metropolitana");
    }

    @Test
    void testObtenerRegionPorCodigo() {
        Region region = new Region();
        region.setId(1L);
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");

        when(regionRepository.findByCodigo("RM")).thenReturn(Optional.of(region));

        RegionDTO resultado = regionService.obtenerRegionPorCodigo("RM");

        assertThat(resultado.getCodigo()).isEqualTo("RM");
        assertThat(resultado.getNombre()).isEqualTo("Región Metropolitana");
    }

    @Test
    void testGuardarRegion() {
        Region region = new Region();
        region.setNombre("Región Metropolitana");
        region.setCodigo("RM");
        region.setOrden(13);

        Region regionGuardada = new Region();
        regionGuardada.setId(1L);
        regionGuardada.setNombre("Región Metropolitana");
        regionGuardada.setCodigo("RM");
        regionGuardada.setOrden(13);

        when(regionRepository.existsByCodigo("RM")).thenReturn(false);
        when(regionRepository.save(any(Region.class))).thenReturn(regionGuardada);

        Region resultado = regionService.guardarRegion(region);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Región Metropolitana");
    }

    @Test
    void testModificarRegion() {
        Region regionExistente = new Region();
        regionExistente.setId(1L);
        regionExistente.setNombre("Región Metropolitana");
        regionExistente.setCodigo("RM");
        regionExistente.setOrden(13);

        Region regionActualizada = new Region();
        regionActualizada.setNombre("Región Metropolitana de Santiago");
        regionActualizada.setCodigo("RMS");
        regionActualizada.setOrden(13);

        when(regionRepository.findById(1L)).thenReturn(Optional.of(regionExistente));
        when(regionRepository.save(any(Region.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Region resultado = regionService.modificarRegion(1L, regionActualizada);

        assertThat(resultado.getNombre()).isEqualTo("Región Metropolitana de Santiago");
        assertThat(resultado.getCodigo()).isEqualTo("RMS");
    }

    @Test
    void testEliminarRegion() {
        when(regionRepository.existsById(1L)).thenReturn(true);

        regionService.eliminarRegion(1L);

        verify(regionRepository).deleteById(1L);
    }
}
