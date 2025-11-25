package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.CiudadRepository;
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
class CiudadServiceTest {

    @Mock
    private CiudadRepository ciudadRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private CiudadService ciudadService;

    @Test
    void obtenerCiudadesPorRegion_returnsDtoList() {
        Region r = new Region(1L, "R", "R1", 1, null, null);
        Ciudad c1 = new Ciudad(1L, "C1", r, null);
        Ciudad c2 = new Ciudad(2L, "C2", r, null);

        when(ciudadRepository.findByRegionIdOrderByNombreAsc(1L)).thenReturn(List.of(c1, c2));

        var result = ciudadService.obtenerCiudadesPorRegion(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRegionId()).isEqualTo(1L);
    }

    @Test
    void guardarCiudad_whenRegionNotExists_throws() {
        Region r = new Region(99L, "R", "R9", 9, null, null);
        Ciudad ciudad = new Ciudad(null, "Nueva", r, null);

        when(regionRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ciudadService.guardarCiudad(ciudad));
    }
}
