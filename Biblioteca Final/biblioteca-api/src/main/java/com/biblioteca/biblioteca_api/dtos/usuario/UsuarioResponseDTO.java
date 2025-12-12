package com.biblioteca.biblioteca_api.dtos.usuario;

import com.biblioteca.biblioteca_api.entity.Usuario;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private Set<String> roles;

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRoles()
                        .stream()
                        .map(r -> r.getNombre().name()) // convierte TipoRol a String
                        .collect(Collectors.toSet()))
                .build();
    }
}
