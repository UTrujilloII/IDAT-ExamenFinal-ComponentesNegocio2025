package pe.edu.idat.biblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Biblioteca IDAT - Documentación") // Título actualizado
                        .version("v1.0")
                        .description("Documentación automática con Swagger - Spring Boot"))

                // --- CONFIGURACIÓN DE SEGURIDAD JWT (NUEVO) ---
                // 1. Añade el requisito de seguridad a nivel global
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // 2. Define cómo se llama y qué tipo de esquema de seguridad es
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // Indica el formato del token
                                        .description("Ingrese el token JWT (Ej: Pegar solo el token)"))
                );
    }
}