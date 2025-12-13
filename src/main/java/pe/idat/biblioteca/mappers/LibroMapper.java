package pe.idat.biblioteca.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.idat.biblioteca.dto.libro.LibroRequest;
import pe.idat.biblioteca.dto.libro.LibroResponse;
import pe.idat.biblioteca.entity.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    @Mapping(target = "titulo")
    LibroResponse toResponse(Libro libro);

    @Mapping(target = "prestamos", ignore = true)
    Libro toEntity(LibroRequest libroRequest);
}
