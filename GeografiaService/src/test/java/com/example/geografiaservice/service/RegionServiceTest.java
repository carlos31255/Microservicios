package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void obtenerTodasLasRegiones_returnDtoList() {
        Region r1 = new Region(1L, "Región A", "RA", 1, null, null);
        Region r2 = new Region(2L, "Región B", "RB", 2, null, null);

        when(regionRepository.findAllByOrderByOrdenAsc()).thenReturn(List.of(r1, r2));

        var result = regionService.obtenerTodasLasRegiones();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNombre()).isEqualTo("Región A");
    }

    @Test
    void guardarRegion_whenCodigoExists_throws() {
        Region region = new Region(null, "X", "XY", 1, null, null);
        when(regionRepository.existsByCodigo("XY")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> regionService.guardarRegion(region));
    }
}
