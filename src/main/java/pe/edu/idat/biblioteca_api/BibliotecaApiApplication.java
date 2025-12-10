package pe.edu.idat.biblioteca_api; // Tu paquete

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pe.edu.idat.biblioteca_api.model.Rol;
import pe.edu.idat.biblioteca_api.repository.RolRepository;

@SpringBootApplication
public class BibliotecaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApiApplication.class, args);
	}

	// Esto se ejecuta al iniciar la app
	@Bean
	public CommandLineRunner initData(RolRepository rolRepository) {
		return args -> {
			if (rolRepository.findByNombre(Rol.TipoRol.ADMIN).isEmpty()) {
				Rol admin = new Rol();
				admin.setNombre(Rol.TipoRol.ADMIN);
				rolRepository.save(admin);
			}
			if (rolRepository.findByNombre(Rol.TipoRol.USUARIO).isEmpty()) {
				Rol user = new Rol();
				user.setNombre(Rol.TipoRol.USUARIO);
				rolRepository.save(user);
			}
		};
	}
}