package pe.edu.idat.biblioteca.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Usuario;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {



    @Mapping(source = "rol.nombre", target = "rol")
    UsuarioResponse toResponse(Usuario usuario);

    Usuario toEntity(UsuarioRequest request);
}