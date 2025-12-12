package com.proyecto.idat.ms_gestion_ventas.controller;

import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthLoginRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthRegisterRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthResponse;
import com.proyecto.idat.ms_gestion_ventas.dto.usuario.UsuarioResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.exception.RecursoNoEncontradoException;
import com.proyecto.idat.ms_gestion_ventas.mappers.UsuarioMapper;
import com.proyecto.idat.ms_gestion_ventas.repository.UsuarioRepository;
import com.proyecto.idat.ms_gestion_ventas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.refreshToken(authHeader));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(@AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el usuario autenticado"
                ));

        return ResponseEntity.ok(usuarioMapper.toResponse(usuario));
    }
}


