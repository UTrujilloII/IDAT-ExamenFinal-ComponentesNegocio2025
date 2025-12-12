package com.biblioteca.api.controller;

import com.biblioteca.api.dto.AuthRequest;
import com.biblioteca.api.dto.AuthResponse;
import com.biblioteca.api.model.Usuario;
import com.biblioteca.api.repository.UsuarioRepository;
import com.biblioteca.api.security.JwtUtils;
import com.biblioteca.api.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Gestión de autenticación")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private com.biblioteca.api.security.CustomUserDetailsService userDetailsService;

    // ===========================================================
    // REGISTRO
    // ===========================================================
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {

        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre("Usuario"); // Puedes mejorarlo si lo deseas
        usuario.setEmail(request.getUsername() + "@mail.com"); // Para evitar errores en BD

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // ===========================================================
    // LOGIN (Corrección aplicada)
    // ===========================================================
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve un token JWT válido por 24 horas")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        // 1. Autenticación
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Cargar UserDetails desde la BD
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // 3. Generar token
        String token = jwtUtils.generateToken(userDetails);

        // 4. Retornar token
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
