package com.proyecto.idat.ms_gestion_ventas.service.impl;

import com.proyecto.idat.ms_gestion_ventas.exception.ReglaNegocioException;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthLoginRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthRegisterRequest;
import com.proyecto.idat.ms_gestion_ventas.dto.auth.AuthResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Rol;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.repository.RolRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.UsuarioRepository;
import com.proyecto.idat.ms_gestion_ventas.security.BloqueoTokenService;
import com.proyecto.idat.ms_gestion_ventas.security.JwtUtils;
import com.proyecto.idat.ms_gestion_ventas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final BloqueoTokenService bloqueoTokenService;

    @Override
    public AuthResponse register(AuthRegisterRequest request) {

        // Validaciones de duplicados antes de grabar
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el username '" + request.username() + "'"
            );
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException(
                    "Ya existe un usuario registrado con el email '" + request.email() + "'"
            );
        }

        Rol rolUsuario = rolRepository.findByNombre("USUARIO")
                .orElseThrow(() -> new RuntimeException("Rol USUARIO no configurado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setNombreCompleto(request.nombreCompleto());
        usuario.setEmail(request.email());
        usuario.setRoles(Set.of(rolUsuario));

        usuarioRepository.save(usuario);

        String token = jwtUtils.generateToken(usuario);
        return new AuthResponse(token);
    }




    @Override
    public AuthResponse login(AuthLoginRequest request) {

        // Primero revisar si el usuario ya esta bloqueado por intentos fallidos
        if (bloqueoTokenService.estaBloqueado(request.username())) {
            long minutosRestantes = bloqueoTokenService.obtenerMinutosRestantes(request.username());
            throw new ReglaNegocioException(
                    "La cuenta del usuario '" + request.username() + "' está bloqueada por múltiples intentos fallidos de contraseña. " +
                            "Vuelve a intentarlo en aproximadamente " + minutosRestantes + " minutos."
            );
        }

        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()
            );
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException ex) {
            boolean seBloqueo = bloqueoTokenService.registrarIntentoFallido(request.username());
            if (seBloqueo) {
                throw new ReglaNegocioException(
                        "La cuenta del usuario '" + request.username() +
                                "' ha sido bloqueada por múltiples intentos fallidos. " +
                                "Vuelve a intentarlo en 30 minutos."
                );
            }
            throw new ReglaNegocioException("Credenciales inválidas");
        }

        // Si llega aqui, el login fue correcto, se limpian intentos fallidos
        bloqueoTokenService.limpiarIntentos(request.username());

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtils.generateToken(usuario);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse refreshToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new RuntimeException("Cabecera Authorization inválida. Debe ser: Bearer <token>");
        }

        String token = bearerToken.substring(7);

        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("Token JWT inválido o expirado");
        }

        String username = jwtUtils.getUsernameFromToken(token);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nuevoToken = jwtUtils.generateToken(usuario);
        return new AuthResponse(nuevoToken);
    }
}
