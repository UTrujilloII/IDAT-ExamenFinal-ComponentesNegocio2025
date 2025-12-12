package com.biblioteca.biblioteca_api.services;

import com.biblioteca.biblioteca_api.entity.Usuario;

import java.util.Arrays;
import java.util.List;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);

    Usuario obtenerPorEmail(String email);

    Usuario obtenerPorId(Long id);

    List<Usuario> listarTodos();

}
