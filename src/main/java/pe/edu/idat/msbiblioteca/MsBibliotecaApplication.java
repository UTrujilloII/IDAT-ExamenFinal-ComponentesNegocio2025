package pe.edu.idat.msbiblioteca;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Punto de entrada de la aplicación MS-Biblioteca.
 * Muestra información mínima sobre endpoints en el arranque y evita
 * exponer credenciales sensibles en los logs.
 */
@SpringBootApplication
@Slf4j
public class MsBibliotecaApplication {

	public static void main(String[] args) {
		log.info("Iniciando MS-Biblioteca Application...");
		ConfigurableApplicationContext context = SpringApplication.run(MsBibliotecaApplication.class, args);

		String port = context.getEnvironment().getProperty("server.port", "9595");
		log.info("Aplicación iniciada exitosamente en puerto: {}", port);
		log.info("URL Base: http://localhost:{}", port);
		log.info("Endpoints de autenticación:");
		log.info("   - POST /v1/auth/login");
		log.info("   - POST /v1/auth/register");
		log.info("Endpoints de libros:");
		log.info("   - GET  /v1/libros");
		log.info("   - POST /v1/libros");
		log.info("Endpoints de préstamos:");
		log.info("   - GET  /v1/prestamos");
		log.info("   - POST /v1/prestamos");
		// No registrar contraseñas en logs por seguridad
		log.info("Usuario por defecto: admin (password no mostrado por seguridad)");
	}

}
