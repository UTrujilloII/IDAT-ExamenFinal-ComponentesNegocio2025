package pe.edu.idat.msbiblioteca.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.msbiblioteca.dto.auth.LoginRequest;
import pe.edu.idat.msbiblioteca.dto.auth.RegisterRequest;
import pe.edu.idat.msbiblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.msbiblioteca.dto.jwt.RefreshTokenRequest;
import pe.edu.idat.msbiblioteca.entity.Rol;
import pe.edu.idat.msbiblioteca.entity.Usuario;
import pe.edu.idat.msbiblioteca.exception.BusinessException;
import pe.edu.idat.msbiblioteca.exception.DuplicateResourceException;
import pe.edu.idat.msbiblioteca.exception.ResourceNotFoundException;
import pe.edu.idat.msbiblioteca.repository.RolRepository;
import pe.edu.idat.msbiblioteca.repository.UsuarioRepository;
import pe.edu.idat.msbiblioteca.security.jwt.JwtUtil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Servicio para gestionar la autenticación y registro de usuarios.
 * Proporciona operaciones de login, registro y renovación de tokens JWT.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Autentica un usuario y genera tokens JWT.
     *
     * @param loginRequest Datos de login (username, password)
     * @return JwtResponse con access token, refresh token, username y rol
     * @throws BusinessException si las credenciales son inválidas
     */
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.username());

        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Buscar usuario
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + loginRequest.username()));

            // Generar tokens
            String accessToken = jwtUtil.generateToken(loginRequest.username());
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest.username());

            // Obtener el primer rol (simplificado)
            String role = usuario.getRoles().isEmpty()
                    ? "USER"
                    : usuario.getRoles().iterator().next().getNombre();

            log.info("Login exitoso para usuario: {}", loginRequest.username());

            return new JwtResponse(
                    accessToken,
                    refreshToken,
                    usuario.getUsername(),
                    role
            );

        } catch (Exception e) {
            log.error("Error en login para usuario: {}", loginRequest.username(), e);
            throw new BusinessException("Credenciales inválidas");
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param registerRequest Datos de registro (username, password, role)
     * @return JwtResponse con tokens generados para el nuevo usuario
     * @throws DuplicateResourceException si el username ya existe
     * @throws ResourceNotFoundException si el rol especificado no existe
     */
    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        log.info("Intento de registro para usuario: {}", registerRequest.username());

        // Validar que el usuario no exista
        if (usuarioRepository.existsByUsername(registerRequest.username())) {
            log.warn("Intento de registro con username existente: {}", registerRequest.username());
            throw new DuplicateResourceException("El usuario ya existe: " + registerRequest.username());
        }

        // Buscar rol
        String roleName = registerRequest.role() != null && !registerRequest.role().isEmpty()
                ? registerRequest.role().toUpperCase()
                : "USUARIO";

        Rol rol = rolRepository.findByNombre(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(registerRequest.username());
        nuevoUsuario.setPassword(passwordEncoder.encode(registerRequest.password()));
        nuevoUsuario.setEnabled(true);
        nuevoUsuario.setFechaRegistro(LocalDate.now());

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        nuevoUsuario.setRoles(roles);

        // Guardar usuario
        usuarioRepository.save(nuevoUsuario);

        log.info("Usuario registrado exitosamente: {}", registerRequest.username());

        // Generar tokens automáticamente
        String accessToken = jwtUtil.generateToken(registerRequest.username());
        String refreshToken = jwtUtil.generateRefreshToken(registerRequest.username());

        return new JwtResponse(
                accessToken,
                refreshToken,
                nuevoUsuario.getUsername(),
                roleName
        );
    }

    /**
     * Renueva el access token usando un refresh token válido.
     *
     * @param refreshTokenRequest Request con el refresh token
     * @return JwtResponse con nuevo access token y el mismo refresh token
     * @throws BusinessException si el refresh token es inválido
     */
    @Transactional(readOnly = true)
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("Intento de renovación de token");

        try {
            String refreshToken = refreshTokenRequest.refreshToken();

            // Validar refresh token
            if (!jwtUtil.validateToken(refreshToken)) {
                log.warn("Refresh token inválido");
                throw new BusinessException("Refresh token inválido o expirado");
            }

            // Extraer username del refresh token
            String username = jwtUtil.extractUsername(refreshToken);

            // Verificar que el usuario existe
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

            // Generar nuevo access token
            String newAccessToken = jwtUtil.generateToken(username);

            String role = usuario.getRoles().isEmpty()
                    ? "USER"
                    : usuario.getRoles().iterator().next().getNombre();

            log.info("Token renovado exitosamente para usuario: {}", username);

            return new JwtResponse(
                    newAccessToken,
                    refreshToken, // El refresh token permanece igual
                    usuario.getUsername(),
                    role
            );

        } catch (Exception e) {
            log.error("Error al renovar token", e);
            throw new BusinessException("Error al renovar el token: " + e.getMessage());
        }
    }
}

