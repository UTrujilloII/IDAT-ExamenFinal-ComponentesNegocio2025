package pe.idat.biblioteca.service.impl;

import pe.idat.biblioteca.dto.auth.LoginRequest;
import pe.idat.biblioteca.dto.auth.RegisterRequest;
import pe.idat.biblioteca.dto.jwt.JwtResponse;
import pe.idat.biblioteca.dto.jwt.RefreshTokenRequest;
import pe.idat.biblioteca.dto.usuario.UsuarioResponse;

import java.util.List;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse registrarUsuario(RegisterRequest request);
    JwtResponse registrarAdmin(RegisterRequest request);
    JwtResponse refreshToken(RefreshTokenRequest request);
    public void desactivarUsuario(Long id);
    public void reactivarUsuario(Long id);
    UsuarioResponse obtenerUsuarioPorId(Long id);
    List<UsuarioResponse> listarTodosLosUsuarios();
}
