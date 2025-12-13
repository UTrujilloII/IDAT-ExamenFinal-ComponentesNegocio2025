package pe.idat.biblioteca.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.idat.biblioteca.dto.auth.LoginRequest;
import pe.idat.biblioteca.dto.auth.RegisterRequest;
import pe.idat.biblioteca.dto.jwt.JwtResponse;
import pe.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.idat.biblioteca.entity.Rol;
import pe.idat.biblioteca.entity.Usuario;
import pe.idat.biblioteca.repository.RolRepository;
import pe.idat.biblioteca.repository.UsuarioRepository;
import pe.idat.biblioteca.security.Jwt.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final JwtUtil jwtUtil;


    @Override
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.username(),
                        request.password()
                )
        );
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow( () -> new RuntimeException("El usuario no existe"));
        String role = usuario.getRoles().iterator().next().getNombre();
        String accessToken = jwtUtil.generateToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());
        return new JwtResponse(accessToken,refreshToken,usuario.getUsername(), role);
    }

    @Override
    public JwtResponse registrarUsuario(RegisterRequest request) {
        if(usuarioRepository.findByUsername(request.username()).isPresent())
        {
            throw new RuntimeException("Por favor ingresar otro username, debido  a que ya se encuentra registrado.");
        }
        if(usuarioRepository.findByEmail(request.email()).isPresent())
        {
            throw new RuntimeException("El email ingresado ya esta registrado");
        }
        Rol rol = rolRepository.findByNombre("USER")
                .orElseThrow( () -> new RuntimeException("El rol ingresado no existe"));
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setEnabled(true);
        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
        String accessToken = jwtUtil.generateToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());
        return  new JwtResponse(accessToken,refreshToken,usuario.getUsername(), "USER");

    }

    @Override
    public JwtResponse registrarAdmin(RegisterRequest request) {
        if(usuarioRepository.findByUsername(request.username()).isPresent())
        {
            throw new RuntimeException("Por favor ingresar otro username, debido  a que ya se encuentra registrado.");
        }
        if(usuarioRepository.findByEmail(request.email()).isPresent())
        {
            throw new RuntimeException("El email ingresado ya esta registrado");
        }
        Rol rol = rolRepository.findByNombre("ADMIN")
                .orElseThrow( () -> new RuntimeException("El rol ingresado no existe"));
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setEnabled(true);
        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
        String accessToken = jwtUtil.generateToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());
        return  new JwtResponse(accessToken,refreshToken,usuario.getUsername(),"ADMIN");
    }



    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        if(!jwtUtil.validateToken(request.refreshToken()))
        {
            throw new RuntimeException("Refresh token inválido");
        }
        String username = jwtUtil.extractUsername(request.refreshToken());
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow( () -> new RuntimeException("El usuario ingreado no existe"));

        String accessToken = jwtUtil.generateToken(usuario.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(usuario.getUsername());
        String role = usuario.getRoles().iterator().next().getNombre();
        return  new JwtResponse(accessToken,refreshToken,usuario.getUsername(), role);
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe"));

        if (!usuario.isEnabled()) {
            throw new IllegalStateException("El usuario ya está desactivado");
        }


        boolean tienePrestamosActivos = usuario.getPrestamos().stream()
                .anyMatch(p -> !p.isDevuelto());

        if (tienePrestamosActivos) {
            throw new IllegalStateException("No se puede desactivar el usuario porque tiene préstamos activos sin devolver");
        }

        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void reactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe"));

        if (usuario.isEnabled()) {
            throw new IllegalStateException("El usuario ya está activo");
        }

        usuario.setEnabled(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + id + " no existe"));
        return convertirAResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.isEnabled(),
                usuario.getRoles().stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet())
        );
    }
}
