package com.proyecto.idat.ms_gestion_ventas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI() {

        //  Esquema de seguridad tipo Bearer JWT para Swagger
        SecurityScheme bearerScheme = new SecurityScheme()
                .name("bearer-jwt")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearer-jwt");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt", bearerScheme))
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("API Gestión de Biblioteca Universitaria")
                        .description("API RESTful para gestionar libros, usuarios, préstamos y devoluciones con seguridad JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo idat")
                                .email("equipo@biblioteca.com")
                        )
                );
    }
}
