package com.proyecto.idat.ms_gestion_ventas.mappers;

import com.proyecto.idat.ms_gestion_ventas.dto.usuario.UsuarioResponse;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {


    @Mapping(target = "nombre", source = "nombreCompleto")
    @Mapping(
            target = "roles",
            expression = "java(usuario.getRoles().stream()" +
                    ".map(rol -> rol.getNombre())" +
                    ".toList())"
    )
    UsuarioResponse toResponse(Usuario usuario);
}
