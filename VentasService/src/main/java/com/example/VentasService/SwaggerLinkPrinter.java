package com.example.ventasservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SwaggerLinkPrinter implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger log = LoggerFactory.getLogger(SwaggerLinkPrinter.class);
    private final Environment env;

    public SwaggerLinkPrinter(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        WebServerApplicationContext ctx = (WebServerApplicationContext) event.getApplicationContext();
        int port = ctx.getWebServer().getPort();
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String host = env.getProperty("server.address", "localhost");
        String path = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui/index.html");
        if (!path.startsWith("/")) path = "/" + path;
        String url = String.format("http://%s:%d%s%s", host, port, contextPath, path);
        log.info("API docs (Swagger UI): {}", url);
    }
}
