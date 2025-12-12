package com.biblioteca.biblioteca_api.mappers;

import com.biblioteca.biblioteca_api.dtos.usuario.UsuarioResponseDTO;
import com.biblioteca.biblioteca_api.entity.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-30T23:35:01-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioResponseDTO.UsuarioResponseDTOBuilder usuarioResponseDTO = UsuarioResponseDTO.builder();

        usuarioResponseDTO.id( usuario.getId() );
        usuarioResponseDTO.nombre( usuario.getNombre() );
        usuarioResponseDTO.email( usuario.getEmail() );
        usuarioResponseDTO.password( usuario.getPassword() );

        usuarioResponseDTO.roles( mapRolesToStrings(usuario.getRoles()) );

        return usuarioResponseDTO.build();
    }
}
