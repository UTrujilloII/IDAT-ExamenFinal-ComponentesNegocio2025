package pe.edu.idat.biblioteca.service.impl;

import java.util.List;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioPatchRequest;
public interface UsuarioService {
    UsuarioResponse crearUsuario(UsuarioRequest request);
    List<UsuarioResponse> listarTodos();
    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);
    UsuarioResponse actualizarParcialUsuario(Long id, UsuarioPatchRequest request);

    void eliminarUsuario(Long id);
}