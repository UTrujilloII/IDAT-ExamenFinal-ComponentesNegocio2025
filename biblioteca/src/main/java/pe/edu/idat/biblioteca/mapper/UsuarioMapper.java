package pe.edu.idat.biblioteca.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);

    Usuario toEntity(UsuarioRequest usuarioRequest);

    @Mapping(target = "idUsuario",ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromRequest(UsuarioRequest usuarioRequest, @MappingTarget Usuario usuario);
}
