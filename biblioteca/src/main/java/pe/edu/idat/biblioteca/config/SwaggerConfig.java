package pe.edu.idat.biblioteca.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "API DE GESTION DE BIBLIOTECA",
                description = "Esta API sirve para manejar una biblioteca.Aquí puedes registrar usuarios, agregar libros, hacer préstamos y devolver libros. Usa tokens (JWT) para que solo personas autorizadas puedan usar ciertas funciones.",
                version="1.0.0"
        )
)
@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig { }
