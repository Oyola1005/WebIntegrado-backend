package com.backend.app.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transportesMirandaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Transportes Interprovincial Miranda")
                        .description("Back-end para la gestión de viajes Lima ↔ Chimbote")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Transportes Miranda")
                                .email("soporte@miranda.pe")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del proyecto académico - Desarrollo Web Integrado")
                );
    }
}
