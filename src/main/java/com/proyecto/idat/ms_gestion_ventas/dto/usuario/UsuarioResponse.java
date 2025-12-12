package com.proyecto.idat.ms_gestion_ventas.dto.usuario;

import java.util.List;

public record UsuarioResponse(
        Long idUsuario,
        String username,
        String nombre,
        String email,
        List<String> roles // nombres de los roles ["ADMIN"] o ["USUARIO"]
) {}
