package pe.edu.idat.biblioteca.service.impl;

import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrarUsuario(UsuarioRequest request);

    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);

    UsuarioResponse obtenerUsuario(Long id);

    List<UsuarioResponse> listarUsuarios();

    void desactivarUsuario(Long id);

    UsuarioResponse obtenerUsuarioPorEmail(String email);
}