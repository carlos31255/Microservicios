package com.example.entregasService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.ventas.url}")
    private String ventasUrl;

    @Value("${services.usuario.url}")
    private String usuarioUrl;

    // 1. Cliente pre-configurado para Ventas
    @Bean(name = "ventasWebClient")
    public WebClient ventasWebClient(WebClient.Builder builder) {
        return builder.baseUrl(ventasUrl).build();
    }

    // 2. Cliente pre-configurado para Usuarios
    @Bean(name = "usuarioWebClient")
    public WebClient usuarioWebClient(WebClient.Builder builder) {
        return builder.baseUrl(usuarioUrl).build();
    }
}
