package com.example.geografiaservice.service;

import com.example.geografiaservice.model.Comuna;
import com.example.geografiaservice.model.Ciudad;
import com.example.geografiaservice.model.Region;
import com.example.geografiaservice.repository.CiudadRepository;
import com.example.geografiaservice.repository.ComunaRepository;
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
class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private CiudadRepository ciudadRepository;

    @InjectMocks
    private ComunaService comunaService;

    @Test
    void buscarComunasPorNombre_returnsDtoList() {
        Region r = new Region(1L, "R", "R1", 1, null, null);
        Ciudad c = new Ciudad(1L, "Ciudad1", r, null);
        Comuna cm = new Comuna(1L, "Comuna1", r, c);

        when(comunaRepository.findByNombreContainingIgnoreCase("Comu")).thenReturn(List.of(cm));

        var result = comunaService.buscarComunasPorNombre("Comu");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Comuna1");
    }

    @Test
    void guardarComuna_whenCiudadNotExists_throws() {
        Region r = new Region(1L, "R", "R1", 1, null, null);
        Ciudad c = new Ciudad(999L, "C999", r, null);
        Comuna comuna = new Comuna(null, "Nueva", r, c);

        when(regionRepository.existsById(1L)).thenReturn(true);
        when(ciudadRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> comunaService.guardarComuna(comuna));
    }

    
}
