package com.biblioteca.biblioteca_api.dtos.usuario;

import com.biblioteca.biblioteca_api.entity.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String nombre;
    private String email;

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNombre(this.nombre);
        usuario.setEmail(this.email);
        return usuario;
    }
}
