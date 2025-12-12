package com.biblioteca.biblioteca_api.services.impl;

import com.biblioteca.biblioteca_api.dtos.auth.AuthRequest;
import com.biblioteca.biblioteca_api.dtos.auth.AuthResponse;
import com.biblioteca.biblioteca_api.dtos.auth.RegisterRequest;
import com.biblioteca.biblioteca_api.entity.Rol;
import com.biblioteca.biblioteca_api.entity.TipoRol;
import com.biblioteca.biblioteca_api.entity.Usuario;
import com.biblioteca.biblioteca_api.repository.RolRepository;
import com.biblioteca.biblioteca_api.repository.UsuarioRepository;
import com.biblioteca.biblioteca_api.security.JwtService;
import com.biblioteca.biblioteca_api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(AuthRequest request) {

        System.out.println("Login: " + request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);

        // EXTRAER ROLES COMO STRINGS
        List<String> roles = usuario.getRoles()
                .stream()
                .map(r -> r.getNombre().name())
                .toList();

        System.out.println("Login exitoso, token generado: " + token);
        System.out.println("Roles enviados: " + roles);

        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                roles
        );
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        System.out.println("Registro iniciado para: " + request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Si no se envían roles → asignar ROLE_USUARIO
        if (request.getRoles() == null || request.getRoles().isEmpty()) {

            Rol rolUsuario = rolRepository.findByNombre(TipoRol.ROLE_USUARIO)
                    .orElseThrow(() -> new RuntimeException("El rol ROLE_USUARIO no existe en la base"));

            usuario.addRol(rolUsuario);
            System.out.println("Asignado rol por defecto: ROLE_USUARIO");

        } else {
            request.getRoles().forEach(rolStr -> {
                TipoRol tipo = TipoRol.valueOf(rolStr);
                Rol rol = rolRepository.findByNombre(tipo)
                        .orElseThrow(() -> new RuntimeException("El rol " + rolStr + " no existe"));

                usuario.addRol(rol);
                System.out.println("Asignado rol: " + rol.getNombre());
            });
        }

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);

        // EXTRAER ROLES COMO LISTA DE STRINGS
        List<String> roles = usuario.getRoles()
                .stream()
                .map(r -> r.getNombre().name())
                .toList();

        System.out.println("Usuario guardado en DB con ID: " + usuario.getId());
        System.out.println("Token generado: " + token);
        System.out.println("Roles enviados: " + roles);

        return new AuthResponse(
                token,
                usuario.getEmail(),
                usuario.getNombre(),
                roles
        );
    }
}
