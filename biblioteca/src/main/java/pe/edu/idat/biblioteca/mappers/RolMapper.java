package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import pe.edu.idat.biblioteca.entity.Rol;

@Mapper(componentModel = "spring")
public interface RolMapper {

    default String toNombre(Rol rol) {
        return rol != null ? rol.getNombre() : null;
    }
}
