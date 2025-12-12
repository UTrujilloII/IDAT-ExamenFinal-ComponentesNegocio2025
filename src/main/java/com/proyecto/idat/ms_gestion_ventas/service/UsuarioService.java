package com.proyecto.idat.ms_gestion_ventas.service;

import com.proyecto.idat.ms_gestion_ventas.dto.usuario.*;
import java.util.Map;


public interface UsuarioService {

    // Desbloquear usuario (login y/o token)
    Map<String, Object> desbloquearUsuario(String username);

    // Crear usuario (ADMIN) con rol ADMIN o USUARIO
    Map<String, Object> crearUsuarioPorAdmin(AdminCrearUsuarioRequest request);

    // Listar todos los usuarios
    Map<String, Object> listarTodosLosUsuarios();

    // Listar usuarios por rol (ADMIN o USUARIO)
    Map<String, Object> listarUsuariosPorRol(String rolNombre);

    // Listar bloqueos (login + tokens)
    Map<String, Object> listarBloqueos();

    // Actualizar la cuenta propia (USUARIO o ADMIN)
    UsuarioResponse actualizarMiCuenta(String usernameActual, ActualizarMiCuentaRequest request);

    // ADMIN actualiza datos de cualquier usuario
    UsuarioResponse actualizarUsuarioPorAdmin(Long idUsuario, ActualizarUsuarioRequest request);

    // ADMIN elimina cuenta de usuario
    Map<String, Object> eliminarUsuarioPorAdmin(Long idUsuario);
}
