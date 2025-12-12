package pe.edu.idat.msbiblioteca.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.idat.msbiblioteca.entity.Rol;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.repository.RolRepository;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carga de datos iniciales...");

        // 1. Crear roles si no existen
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");

        // 2. Crear usuario admin si no existe
        createUserIfNotExists("admin", "admin123", "ROLE_ADMIN");

        // 3. Crear usuario normal si no existe
        createUserIfNotExists("user1", "user123", "ROLE_USER");

        log.info("Datos iniciales cargados correctamente");
    }

    private void createRoleIfNotExists(String roleName) {
        Optional<Rol> existingRole = rolRepository.findByNombre(roleName);
        if (existingRole.isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(roleName);
            rolRepository.save(rol);
            log.info("Rol creado: {}", roleName);
        } else {
            log.info("Rol ya existe: {}", roleName);
        }
    }

    private void createUserIfNotExists(String username, String plainPassword, String roleName) {
        Optional<Usuario> existingUser = usuarioRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            Rol rol = rolRepository.findByNombre(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            // Se encripta la contraseña
            usuario.setPassword(passwordEncoder.encode(plainPassword));
            usuario.setEnabled(true);
            usuario.getRoles().add(rol);

            usuarioRepository.save(usuario);

            log.info("Usuario creado: {} con contraseña encriptada", username);
        } else {
            log.info("Usuario ya existe: {}", username);
        }
    }
}
