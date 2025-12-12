package pe.edu.idat.biblioteca;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.idat.biblioteca.entity.Libro;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.repository.LibroRepository;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;
import java.util.Set;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
        System.out.println("\n📚 SERVICIO BIBLIOTECA INICIADO → http://localhost:9596/swagger-ui/index.html\n");
    }

    @Bean
    public CommandLineRunner initData(RolRepository rolRepo, UsuarioRepository usuarioRepo,
                                      LibroRepository libroRepo, PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. CREACIÓN DE ROLES (ADMIN y USER)
            Rol rolAdmin = rolRepo.findByNombreIgnoreCase("ADMIN").orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("ADMIN");
                return rolRepo.save(r);
            });

            Rol rolUser = rolRepo.findByNombreIgnoreCase("USER").orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("USER");
                return rolRepo.save(r);
            });

            // 2. CREACIÓN DEL USUARIO ADMIN
            if (!usuarioRepo.existsByUsername("admin")) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setNombre("Administrador Central");
                admin.setDni("11111111");
                admin.setEmail("admin@biblioteca.com");
                admin.setTelefono("900000000");
                admin.setRoles(Set.of(rolAdmin));
                usuarioRepo.save(admin);
                System.out.println("✔ ADMIN CREADO → user: admin | pass: 123456");
            }

            // 3. CREACIÓN DEL USUARIO ESTÁNDAR
            if (!usuarioRepo.existsByUsername("user")) {
                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setNombre("Estudiante General");
                user.setDni("22222222");
                user.setEmail("user@biblioteca.com");
                user.setTelefono("911111111");

                user.setRoles(Set.of(rolUser));
                usuarioRepo.save(user);

                System.out.println("✔ USER CREADO → user: user | pass: 123456");
            }

            // 4. CREACIÓN DE LIBROS INICIALES
            if (libroRepo.count() == 0) {
                Libro l1 = new Libro();
                l1.setTitulo("Cien años de soledad");
                l1.setAutor("Gabriel García Márquez");
                l1.setEditorial("Sudamericana");
                l1.setAnioPublicacion(1967);
                l1.setIsbn("978-0307474728");
                l1.setCantidad(5);
                libroRepo.save(l1);

                Libro l2 = new Libro();
                l2.setTitulo("El Quijote");
                l2.setAutor("Miguel de Cervantes");
                l2.setEditorial("Planeta");
                l2.setAnioPublicacion(1605);
                l2.setIsbn("978-8424113267");
                l2.setCantidad(3);
                libroRepo.save(l2);

                Libro l3 = new Libro();
                l3.setTitulo("1984");
                l3.setAutor("George Orwell");
                l3.setEditorial("Debolsillo");
                l3.setAnioPublicacion(1949);
                l3.setIsbn("978-0451524935");
                l3.setCantidad(8);
                libroRepo.save(l3);

                System.out.println("📚 Libros iniciales creados");
            }
        };
    }
}