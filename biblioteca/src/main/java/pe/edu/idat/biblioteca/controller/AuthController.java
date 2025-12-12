package pe.edu.idat.biblioteca.controller;

import jakarta.validation.Valid; // <--- Importación necesaria
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.idat.biblioteca.dto.auth.AuthResponse;
import pe.edu.idat.biblioteca.dto.auth.LoginRequest;
import pe.edu.idat.biblioteca.dto.auth.RegisterRequest;
import pe.edu.idat.biblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.edu.idat.biblioteca.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Login recibido: " + request);
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) { // <--- @Valid añadido
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}