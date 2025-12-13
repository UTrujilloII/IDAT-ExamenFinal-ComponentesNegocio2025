package pe.idat.biblioteca.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.idat.biblioteca.dto.auth.LoginRequest;
import pe.idat.biblioteca.dto.auth.RegisterRequest;
import pe.idat.biblioteca.dto.jwt.JwtResponse;
import pe.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.idat.biblioteca.service.impl.AuthService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/registrar")
    public ResponseEntity<JwtResponse> registrarUsuario(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.registrarUsuario(registerRequest));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponse usuario = authService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listarTodosLosUsuarios() {
        List<UsuarioResponse> usuarios = authService.listarTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar/Admin")
    public ResponseEntity<JwtResponse> registrarAdmin(@RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(authService.registrarUsuario(registerRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> desactivarUsuario(@PathVariable Long id) {
        authService.desactivarUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario desactivado correctamente"));
    }


    @PatchMapping("/reactivar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reactivarUsuario(@PathVariable Long id) {
        authService.reactivarUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario reactivado correctamente"));
    }
}
