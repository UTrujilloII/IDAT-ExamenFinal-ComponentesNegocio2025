package pe.edu.idat.biblioteca.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.idat.biblioteca.dto.auth.AuthResponse;
import pe.edu.idat.biblioteca.dto.auth.LoginRequest;
import pe.edu.idat.biblioteca.dto.jwt.JwtResponse;
import pe.edu.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.exception.InvalidRefreshException;
import pe.edu.idat.biblioteca.exception.UsuarioNotFoundException;
import pe.edu.idat.biblioteca.repository.RolRepository;
import pe.edu.idat.biblioteca.repository.UsuarioRepository;
import pe.edu.idat.biblioteca.security.JwtUtil;
import pe.edu.idat.biblioteca.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Transactional(readOnly = true)
    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));

        Usuario usuario= usuarioRepository.findByEmail(loginRequest.email())
                .orElseThrow(()-> new UsernameNotFoundException("el usuario no existe"));

        String role=usuario.getRoles().iterator().next().getNombre();
        String accessToken=jwtUtil.generateToken(usuario.getEmail());
        String refreshToken= jwtUtil.generateRefreshToken(usuario.getEmail());

        return new JwtResponse(accessToken,refreshToken,usuario.getEmail(),role);
    }

    @Transactional(readOnly = true)
    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        if(!jwtUtil.validateToken(request.refreshToken())){
            throw new InvalidRefreshException("refresh token invalido");
        }
        String email=jwtUtil.extractEmail(request.refreshToken());
        Usuario usuario =usuarioRepository.findByEmail(email)
                .orElseThrow(()->new UsuarioNotFoundException("el usuario ingresado no existe"));

        String accessToken=jwtUtil.generateToken(usuario.getEmail());
        String refreshToken= jwtUtil.generateRefreshToken(usuario.getEmail());
        String role=usuario.getRoles().iterator().next().getNombre();

        return new JwtResponse(accessToken,refreshToken,usuario.getEmail(),role);
    }

}
