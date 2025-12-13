package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioRequest;
import pe.edu.idat.biblioteca.dto.usuario.UsuarioResponse;
import pe.edu.idat.biblioteca.entity.Rol;
import pe.edu.idat.biblioteca.entity.Usuario;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "roles", source = "roles")
    UsuarioResponse toResponse(Usuario usuario);
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    Usuario toEntity(UsuarioRequest request);

    default Set<String> mapRoles(Set<Rol> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet());
    }
}