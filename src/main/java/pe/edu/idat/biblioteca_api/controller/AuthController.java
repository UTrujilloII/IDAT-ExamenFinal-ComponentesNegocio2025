package pe.edu.idat.biblioteca_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca_api.dto.AuthResponse;
import pe.edu.idat.biblioteca_api.dto.LoginRequest;
import pe.edu.idat.biblioteca_api.dto.RegisterRequest;
import pe.edu.idat.biblioteca_api.model.Rol;
import pe.edu.idat.biblioteca_api.model.Usuario;
import pe.edu.idat.biblioteca_api.repository.RolRepository;
import pe.edu.idat.biblioteca_api.repository.UsuarioRepository;
import pe.edu.idat.biblioteca_api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios") // <--- Título en Swagger
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 1. Registro de Usuario Normal (Rol: USUARIO)
    @Operation(summary = "Registrar Usuario", description = "Crea un nuevo usuario con rol USUARIO por defecto.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usuario.setEmail(registerRequest.getEmail());
        usuario.setNombres(registerRequest.getNombres());
        usuario.setApellidos(registerRequest.getApellidos());

        // Asignar rol por defecto USUARIO
        Rol userRole = rolRepository.findByNombre(Rol.TipoRol.USUARIO)
                .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));
        Set<Rol> roles = new HashSet<>();
        roles.add(userRole);
        usuario.setRoles(roles);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // 2. Registro de Administrador (Rol: ADMIN) - ¡NUEVO!
    // Usar este endpoint para crear el usuario que gestionará los libros
    @Operation(summary = "Registrar Admin", description = "Crea un usuario con privilegios de ADMINISTRADOR.")
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usuario.setEmail(registerRequest.getEmail());
        usuario.setNombres(registerRequest.getNombres());
        usuario.setApellidos(registerRequest.getApellidos());

        // Asignar rol ADMIN
        Rol adminRole = rolRepository.findByNombre(Rol.TipoRol.ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));
        Set<Rol> roles = new HashSet<>();
        roles.add(adminRole);
        usuario.setRoles(roles);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Administrador registrado exitosamente");
    }

    // 3. Login (Generar Token JWT)
    @Operation(summary = "Login de Usuario", description = "Autentica al usuario y devuelve un token JWT Bearer.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createToken(@RequestBody LoginRequest loginRequest) {
        // Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Cargar detalles y generar token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}