package pe.edu.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.edu.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.edu.idat.biblioteca.entity.Prestamo;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {
    @Mapping(target = "usuarioId", source = "usuario.id")
    @Mapping(target = "username", source = "usuario.username")
    @Mapping(target = "libroId", source = "libro.id")
    @Mapping(target = "tituloLibro", source = "libro.titulo")
    @Mapping(target = "estado", source = "estado")

    PrestamoResponse toResponse(Prestamo prestamo);
}