package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.auth.AuthResponse;
import pe.edu.idat.biblioteca.dto.auth.LoginRequest;
import pe.edu.idat.biblioteca.dto.auth.RegisterRequest;
import pe.edu.idat.biblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;
import pe.edu.idat.biblioteca.security.jwt.JwtUtil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User userDetails = new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                        .collect(Collectors.toList())
        );
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> "ROLE_" + rol.getNombre())
                .collect(Collectors.toSet());

        return new AuthResponse(token, refreshToken, usuario.getUsername(), roles);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String usernameFromEmail = request.email().split("@")[0];

        // --- 1. VALIDACIÓN DE UNICIDAD (DNI, EMAIL/USERNAME y TELÉFONO) ---
        if (usuarioRepository.existsByDni(request.dni())) {
            throw new RuntimeException("El DNI ya está registrado.");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        if (usuarioRepository.existsByUsername(usernameFromEmail)) {
            throw new RuntimeException("El nombre de usuario derivado del email ya existe.");
        }
        if (usuarioRepository.existsByTelefono(request.telefono())) {
            throw new RuntimeException("El número de teléfono ya está registrado.");
        }

        // --- 2. ASIGNACIÓN Y FORZADO DE ROL ---
        String rolBuscado = "USER";

        Rol rol = rolRepository.findByNombreIgnoreCase(rolBuscado)
                .orElseThrow(() -> new NoSuchElementException("Rol USER no encontrado en la base de datos."));

        Usuario usuario = new Usuario();

        // --- 3. ASIGNACIÓN DE CAMPOS DE LA LIBRERÍA ---
        usuario.setDni(request.dni());
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());

        // --- 4. ASIGNACIÓN DE CREDENCIALES ---
        usuario.setUsername(usernameFromEmail);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.getRoles().add(rol);

        usuarioRepository.save(usuario);
        User userDetails = new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre()))
                        .collect(Collectors.toList())
        );

        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new AuthResponse(token, refreshToken, usuario.getUsername(),
                Set.of("ROLE_" + rol.getNombre()));
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtUtil.extractUsername(request.refreshToken());

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        User userDetails = new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getNombre()))
                        .collect(Collectors.toList())
        );

        if (username == null || !jwtUtil.validateToken(request.refreshToken(), userDetails)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        String newToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new JwtResponse(newToken, newRefreshToken);
    }
}