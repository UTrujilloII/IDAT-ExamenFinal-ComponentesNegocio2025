package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.usuario.UsuarioResponseDTO;
import com.biblioteca.biblioteca_api.entity.Rol;
import com.biblioteca.biblioteca_api.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;


@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface UsuarioMapper {


    @Mapping(target = "roles", expression = "java(mapRolesToStrings(usuario.getRoles()))")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    // Método auxiliar para convertir Set<Rol> → Set<String>
    default java.util.Set<String> mapRolesToStrings(java.util.Set<Rol> roles) {
        return roles.stream()
                .map(rol -> rol.getNombre().name()) // Enum → String
                .collect(java.util.stream.Collectors.toSet());
    }
}
