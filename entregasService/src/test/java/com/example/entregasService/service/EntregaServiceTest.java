package com.example.entregasService.service;

import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.dto.externo.BoletaExternaDTO;
import com.example.entregasService.model.Entrega;
import com.example.entregasService.repository.EntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {

    @Mock
    private EntregaRepository entregaRepository;

    @Mock
    private WebClient ventasWebClient;

    @Mock
    private WebClient usuarioWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private EntregaService entregaService;

    private Entrega entrega;
    private EntregaRequestDTO entregaRequestDTO;

    @BeforeEach
    void setUp() {
        entrega = new Entrega();
        entrega.setIdEntrega(1L);
        entrega.setIdBoleta(100L);
        entrega.setIdTransportista(5L);
        entrega.setEstadoEntrega("pendiente");
        entrega.setDireccionEntrega("Av. Principal 123");
        entrega.setIdComuna(101L);
        entrega.setFechaAsignacion(LocalDateTime.now());

        entregaRequestDTO = new EntregaRequestDTO();
        entregaRequestDTO.setIdBoleta(100L);
        entregaRequestDTO.setIdTransportista(5L);
        entregaRequestDTO.setEstadoEntrega("pendiente");
        entregaRequestDTO.setObservacion("Nueva entrega");
    }

    @Test
    void testObtenerTodasLasEntregas() {
        Entrega entrega2 = new Entrega();
        entrega2.setIdEntrega(2L);
        entrega2.setIdBoleta(200L);
        entrega2.setEstadoEntrega("en_camino");

        when(entregaRepository.findAll()).thenReturn(Arrays.asList(entrega, entrega2));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        List<EntregaDTO> resultado = entregaService.obtenerTodasLasEntregas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdEntrega()).isEqualTo(1L);
        assertThat(resultado.get(1).getIdEntrega()).isEqualTo(2L);
        verify(entregaRepository).findAll();
    }

    @Test
    void testObtenerEntregaPorId() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.obtenerEntregaPorId(1L);

        assertThat(resultado.getIdEntrega()).isEqualTo(1L);
        assertThat(resultado.getIdBoleta()).isEqualTo(100L);
        assertThat(resultado.getEstadoEntrega()).isEqualTo("pendiente");
        verify(entregaRepository).findById(1L);
    }

    @Test
    void testObtenerEntregaPorId_noEncontrada() {
        when(entregaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entregaService.obtenerEntregaPorId(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Entrega no encontrada");
    }

    @Test
    void testCrearEntrega() {
        Entrega entregaGuardada = new Entrega();
        entregaGuardada.setIdEntrega(1L);
        entregaGuardada.setIdBoleta(100L);
        entregaGuardada.setIdTransportista(5L);
        entregaGuardada.setEstadoEntrega("pendiente");
        entregaGuardada.setFechaAsignacion(LocalDateTime.now());

        when(entregaRepository.save(any(Entrega.class))).thenReturn(entregaGuardada);
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.crearEntrega(entregaRequestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdEntrega()).isEqualTo(1L);
        assertThat(resultado.getIdBoleta()).isEqualTo(100L);
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void testGetEntregasByTransportista() {
        Entrega entrega2 = new Entrega();
        entrega2.setIdEntrega(2L);
        entrega2.setIdBoleta(200L);
        entrega2.setIdTransportista(5L);
        entrega2.setEstadoEntrega("en_camino");

        when(entregaRepository.findByIdTransportista(5L)).thenReturn(Arrays.asList(entrega, entrega2));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        List<EntregaDTO> resultado = entregaService.getEntregasByTransportista(5L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdTransportista()).isEqualTo(5L);
        assertThat(resultado.get(1).getIdTransportista()).isEqualTo(5L);
        verify(entregaRepository).findByIdTransportista(5L);
    }

    @Test
    void testGetEntregasByEstado() {
        Entrega entrega2 = new Entrega();
        entrega2.setIdEntrega(2L);
        entrega2.setIdBoleta(200L);
        entrega2.setEstadoEntrega("pendiente");

        when(entregaRepository.findByEstadoEntrega("pendiente")).thenReturn(Arrays.asList(entrega, entrega2));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        List<EntregaDTO> resultado = entregaService.getEntregasByEstado("pendiente");

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getEstadoEntrega()).isEqualTo("pendiente");
        assertThat(resultado.get(1).getEstadoEntrega()).isEqualTo("pendiente");
        verify(entregaRepository).findByEstadoEntrega("pendiente");
    }

    @Test
    void testAsignarTransportista() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        
        // Mock para validación del transportista
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecUsuario = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecUsuario = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecUsuario = mock(WebClient.ResponseSpec.class);
        
        when(usuarioWebClient.get()).thenReturn(requestHeadersUriSpecUsuario);
        when(requestHeadersUriSpecUsuario.uri(anyString())).thenReturn(requestHeadersSpecUsuario);
        when(requestHeadersSpecUsuario.retrieve()).thenReturn(responseSpecUsuario);
        when(responseSpecUsuario.toBodilessEntity()).thenReturn(Mono.just(mock(org.springframework.http.ResponseEntity.class)));
        
        when(entregaRepository.save(any(Entrega.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Mock para enriquecimiento del DTO
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.asignarTransportista(1L, 5L);

        assertThat(resultado.getIdTransportista()).isEqualTo(5L);
        assertThat(resultado.getEstadoEntrega()).isEqualTo("pendiente");
        assertThat(resultado.getFechaAsignacion()).isNotNull();
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void testAsignarTransportista_entregaNoEncontrada() {
        when(entregaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entregaService.asignarTransportista(999L, 5L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Entrega no encontrada");
    }

    @Test
    void testCompletarEntrega() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(Entrega.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.completarEntrega(1L, "Entrega exitosa");

        assertThat(resultado.getEstadoEntrega()).isEqualTo("entregada");
        assertThat(resultado.getObservacion()).isEqualTo("Entrega exitosa");
        assertThat(resultado.getFechaEntrega()).isNotNull();
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void testCompletarEntrega_sinObservacion() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(Entrega.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.completarEntrega(1L, null);

        assertThat(resultado.getEstadoEntrega()).isEqualTo("entregada");
        assertThat(resultado.getObservacion()).isNull();
        assertThat(resultado.getFechaEntrega()).isNotNull();
    }

    @Test
    void testCompletarEntrega_noEncontrada() {
        when(entregaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entregaService.completarEntrega(999L, "Observación"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Entrega no encontrada");
    }

    @Test
    void testCambiarEstado() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(Entrega.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ventasWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BoletaExternaDTO.class)).thenReturn(Mono.empty());

        EntregaDTO resultado = entregaService.cambiarEstado(1L, "en_camino");

        assertThat(resultado.getEstadoEntrega()).isEqualTo("en_camino");
        verify(entregaRepository).save(any(Entrega.class));
    }

    @Test
    void testCambiarEstado_noEncontrada() {
        when(entregaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> entregaService.cambiarEstado(999L, "en_camino"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Entrega no encontrada");
    }

    @Test
    void testGetEntregasByTransportista_listaVacia() {
        when(entregaRepository.findByIdTransportista(99L)).thenReturn(Arrays.asList());

        List<EntregaDTO> resultado = entregaService.getEntregasByTransportista(99L);

        assertThat(resultado).isEmpty();
        verify(entregaRepository).findByIdTransportista(99L);
    }

    @Test
    void testGetEntregasByEstado_listaVacia() {
        when(entregaRepository.findByEstadoEntrega("cancelada")).thenReturn(Arrays.asList());

        List<EntregaDTO> resultado = entregaService.getEntregasByEstado("cancelada");

        assertThat(resultado).isEmpty();
        verify(entregaRepository).findByEstadoEntrega("cancelada");
    }
}
