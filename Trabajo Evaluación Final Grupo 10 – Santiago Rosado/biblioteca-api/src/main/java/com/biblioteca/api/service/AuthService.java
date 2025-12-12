package com.biblioteca.api.service;

import com.biblioteca.api.dto.auth.AuthRequest;
import com.biblioteca.api.dto.auth.AuthResponse;
import com.biblioteca.api.model.Usuario;
import com.biblioteca.api.repository.UsuarioRepository;
import com.biblioteca.api.security.CustomUserDetails;
import com.biblioteca.api.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * =========================================================
 * AuthService
 * Maneja la lógica de:
 *  - Registro de usuarios nuevos
 *  - Autenticación (login)
 *  - Generación de token JWT
 * =========================================================
 */
@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    // =====================================================
    // REGISTRO DE USUARIO
    // =====================================================
    public String register(Usuario usuario) {

        // Cifrar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuarioRepository.save(usuario);

        return "Usuario registrado con éxito";
    }

    // =====================================================
    // LOGIN → AUTENTICACIÓN → TOKEN JWT
    // =====================================================
    public AuthResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Obtener detalles del usuario autenticado
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        // Crear token JWT
        String token = jwtUtils.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
