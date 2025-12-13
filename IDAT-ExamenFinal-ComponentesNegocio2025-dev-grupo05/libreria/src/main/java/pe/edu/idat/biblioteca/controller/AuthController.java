package pe.edu.idat.biblioteca.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.idat.biblioteca.dto.auth.AuthResponse;
import pe.edu.idat.biblioteca.dto.auth.LoginRequest;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.security.JwtUtil;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    // CORRECCIÓN 1: Inyección correcta usando @Qualifier
    // Asumiendo que el nombre del bean es 'userDetailsServiceLmpi'
    @Qualifier("userDetailsServiceLmpi")
    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        // 1. Intenta autenticar (Lanza excepción si falla la contraseña)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. Carga los detalles del usuario y haz CAST a tu clase Usuario
        final UserDetails details = userDetailsService.loadUserByUsername(request.email());
        final Usuario usuario = (Usuario) details;

        // 3. Genera el JWT y el Refresh Token (si tienes la lógica)
        final String jwt = jwtUtil.generateToken(usuario);
        final String refreshToken = "GENERAR_REFRESH_TOKEN_AQUI"; // Temporal o usa tu lógica

        // 4. CORRECCIÓN 2: Llenar los 8 argumentos de AuthResponse (CRÍTICO)
        return ResponseEntity.ok(new AuthResponse(
                // 1. token
                jwt,
                // 2. refreshToken
                refreshToken,
                // 3. tipo
                "Bearer",
                // 4. id
                usuario.getId(),
                // 5. nombre
                usuario.getNombre(),
                // 6. apellido
                usuario.getApellido(),
                // 7. email
                usuario.getEmail(),
                // 8. rol
                usuario.getRol().getNombre()
        ));
    }
}